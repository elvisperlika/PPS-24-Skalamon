package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Turn
}
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.*

/** Mixin of [[EventManager]] for events pre-configuration.
  */
trait BattleConfiguration(battle: Battle) extends EventManager:
  watch(Ended) { t =>
    checkGameOver(t)
  }

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
              case Some(assignedStatus) =>
                assignedStatus.status.executeEffect(resetFlags)
              case None => resetFlags

            val afterVolatile =
              afterNonVolatile.volatileStatus.foldLeft(afterNonVolatile) {
                (current, assignedStatus) =>
                  assignedStatus.status.executeEffect(
                    current,
                    battle.turnIndex,
                    assignedStatus.turnAssigned
                  )
              }
            val cleaned = removeExpiredStatuses(afterVolatile)

            val updatedTeam = trainer.team.map {
              case p if p.id == cleaned.id => cleaned
              case p                       => p
            }

            trainer.copy(team = updatedTeam)

          case None => trainer
      }

      bt.copy(trainers = updatedTrainers)

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
