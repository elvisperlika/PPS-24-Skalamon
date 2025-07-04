package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Turn,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.*

trait BattleConfiguration(battle: Battle) extends EventManager:

  watch(Ended) { turn => checkGameOver(turn) }

  /** Handles the end of a turn, executing any status effects and checking for
    * game over conditions.
    * @param turn
    *   The current turn being processed.
    */
  private def checkGameOver(turn: Turn): Unit =
    val aliveTrainers =
      turn.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
    aliveTrainers match
      case List(winner) => battle.gameState = GameOver(Some(winner))
      case Nil          => battle.gameState = GameOver(None)
      case _            =>

object StatusExecutor:

  private val BurnAttackReduction = 2
  private val BurnDamageReduction = 16
  private val ParalyzeAttackReduction = 2
  private val ParalyzeTriggerChance = 0.25
  private val SleepTurns = 3
  private val FreezeThawChance = 0.2
  private val PoisonedDamageReduction = 16
  private val BadlyPoisonedDamageReduction = 16

  extension (battle: Battle)
    /** Executes the status effects for each Pokémon in the battle state. This
      * method is called at the end of each turn to apply non-volatile and
      * volatile status effects.
      *
      * @param bt
      *   The current battle state containing all trainers and their Pokémon.
      * @return
      *   The updated battle state with applied status effects.
      */
    def executeStatus(bt: BattleState): BattleState =
      val updatedTrainers = bt.trainers.map { trainer =>
        trainer.inField match
          case Some(inFieldPokemon) =>
            val original = trainer.team.find(_.id == inFieldPokemon.id).get
            val resetFlags =
              original.copy(isProtected = false, skipsCurrentTurn = false)

            val afterNonVolatile = resetFlags.nonVolatileStatus match
              case Some(status) => executeNonVolatileStatus(resetFlags, status)
              case None         => resetFlags

            val afterVolatile = executeVolatileStatus(
              afterNonVolatile,
              afterNonVolatile.volatileStatus
            )
            val cleaned = removeExpiredStatuses(afterVolatile)

            val updatedTeam = trainer.team.map {
              case p if p.id == cleaned.id => cleaned
              case p                       => p
            }

            trainer.copy(team = updatedTeam)

          case None => trainer
      }

      bt.copy(trainers = updatedTrainers)

    /** Executes the non-volatile status effects for a Pokémon.
      *
      * @param pk
      *   The Pokémon whose non-volatile status is being executed.
      * @param status
      *   The non-volatile status assigned to the Pokémon.
      * @return
      *   The updated Pokémon after applying the non-volatile status effects.
      */
    private def executeNonVolatileStatus(
        pk: BattlePokemon,
        status: AssignedStatus[NonVolatileStatus]
    ): BattlePokemon = status.status match

      // TODO: Vedere se lasciare la clusola di "Guts"
      // Deals damage: 1/16 of max HP at end of each turn.
      // Halves physical attack stat (unless the Pokémon has the ability Guts)
      case Burn =>
        val updatedStats = pk.base.baseStats.base.updatedWith(Stat.Attack) {
          case Some(value) if pk.base.ability.name != "Guts" =>
            Some(value / BurnAttackReduction)
          case other => other
        }
        pk.copy(
          currentHP = pk.currentHP - (pk.base.hp / BurnDamageReduction),
          base =
            pk.base.copy(baseStats =
              pk.base.baseStats.copy(base = updatedStats)
            )
        )

      // 25% chance to be fully unable to act each turn.
      // Reduces Speed to 50%.
      case Paralyze =>
        val updatedStats = pk.base.baseStats.base.updatedWith(Stat.Speed) {
          case Some(value) => Some(value / ParalyzeAttackReduction)
          case other       => other
        }
        pk.copy(
          base =
            pk.base.copy(baseStats =
              pk.base.baseStats.copy(base = updatedStats)
            ),
          skipsCurrentTurn =
            scala.util.Random.nextDouble() < ParalyzeTriggerChance
        )

      // Pokémon cannot act for 3 turns.
      // Turns spent asleep are tracked even if switched out.
      case Sleep =>
        if battle.turnIndex - status.turnAssigned < SleepTurns then
          pk.copy(skipsCurrentTurn = true)
        else pk

      // Pokémon cannot move while frozen.
      // 20% chance each turn to thaw naturally.
      case Freeze =>
        if scala.util.Random.nextDouble() < FreezeThawChance then
          pk.copy(nonVolatileStatus = None)
        else pk

      // Takes 1/16 max HP damage per turn.
      // Persists indefinitely until cured.
      case Poison =>
        pk.copy(currentHP =
          pk.currentHP - (pk.base.hp / PoisonedDamageReduction)
        )

      // Damage escalates: 1/16 HP first turn, then increases by 1/16 each turn on the field (e.g., 1/8, 3/16, etc.).
      // Caused only by the Toxic move or its variants (e.g., Toxic Spikes).
      case BadlyPoison =>
        val turnsPoisoned = battle.turnIndex - status.turnAssigned
        val damage = pk.base.hp * turnsPoisoned / BadlyPoisonedDamageReduction
        pk.copy(currentHP = pk.currentHP - damage)

      case _ => pk

    /** Executes the volatile status effects for a Pokémon.
      *
      * @param pk
      *   The Pokémon whose volatile status is being executed.
      * @param statuses
      *   The set of volatile statuses assigned to the Pokémon.
      * @return
      *   The updated Pokémon after applying the volatile status effects.
      */
    private def executeVolatileStatus(
        pk: BattlePokemon,
        statuses: Set[AssignedStatus[VolatileStatus]]
    ): BattlePokemon =
      statuses.foldLeft(pk) { (current, assignedStatus) =>
        assignedStatus.status match

          // After 1 turn, the target Pokémon will fall asleep, unless switched out.
          case Yawn if battle.turnIndex == assignedStatus.turnAssigned + 1 =>
            current.copy(skipsCurrentTurn = true)

          // Blocks moves that target this Pokémon.
          case ProtectEndure =>
            current.copy(isProtected = true)

          case _ => current
      }

    /** Removes expired volatile statuses from a Pokémon.
      *
      * @param pk
      *   The Pokémon from which to remove expired statuses.
      * @return
      *   The Pokémon with expired statuses removed.
      */
    private def removeExpiredStatuses(pk: BattlePokemon): BattlePokemon =
      val updatedVolatileStatuses = pk.volatileStatus.filterNot {
        case AssignedStatus(status: Expirable, _) =>
          status.isExpired(battle.turnIndex)
        case _ => false
      }
      pk.copy(volatileStatus = updatedVolatileStatuses)
