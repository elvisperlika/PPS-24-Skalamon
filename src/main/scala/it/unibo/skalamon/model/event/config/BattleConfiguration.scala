package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.model.battle.{Battle, Turn}
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.*

/** Mixin of [[EventManager]] for events pre-configuration.
  */
trait BattleConfiguration(battle: Battle) extends EventManager:
  watch(Ended) { t =>
    checkGameOver(t)
  }

  private val BurnAttackReduction = 2
  private val BurnDamageReduction = 16

  private val ParalyzeAttackReduction = 2

  private val FreezeThawChance = 0.2

  private val PoisonedDamageReduction = 16
  private val BadlyPoisonedDamageReduction = 16

  private def checkGameOver(turn: Turn): Unit =
    val aliveTrainers =
      turn.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
    aliveTrainers match
      case List(winner) => battle.gameState = GameOver(Some(winner))
      case Nil          => battle.gameState = GameOver(None)
      case _            =>

  watch(Ended)(executeStatus)

  private def executeStatus(turn: Turn): Unit =
    turn.state.snapshot.trainers.foreach: trainer =>
      trainer.inField match
        case Some(pokemon) =>
          pokemon.nonVolatileStatus match
            case Some(status) =>
              executeNonVolatileStatus(pokemon, status, turn)
            case None => ()
        case None => ()

  private def executeNonVolatileStatus(
      pk: BattlePokemon,
      status: AssignedStatus[NonVolatileStatus],
      turn: Turn
  ): BattlePokemon = status.status match
    
    // Deals damage: 1/16 of max HP at end of each turn.
    // Halves physical attack stat (unless the Pokémon has the ability Guts)
    case Burn =>
      val updatedStats = pk.base.baseStats.base.updatedWith(Stat.Attack):
        case Some(value) if pk.base.ability.name != "Guts" =>
          Some(value / BurnAttackReduction)
        case other => other
      pk.copy(
        currentHP = pk.currentHP - (pk.base.hp / BurnDamageReduction),
        base = pk.base.copy(
          baseStats = pk.base.baseStats.copy(base = updatedStats)
        )
      )

    // TODO: non so come gesitre la cosa del non puo attaccare
    // 25% chance to be fully unable to act each turn.
    // Reduces Speed to 50%.
    case Paralyze =>
      val updatedStats = pk.base.baseStats.base.updatedWith(Stat.Speed):
        case Some(value) => Some(value / ParalyzeAttackReduction)
        case other       => other
      pk.copy(
        base = pk.base.copy(
          baseStats = pk.base.baseStats.copy(base = updatedStats)
        )
      )

    // TODO: Non so come gestirlo
    // Pokémon cannot act for 1 to 3 turns.
    // Turns spent asleep are tracked even if switched out.
    // Moves like Rest auto-induce sleep but heal HP to 100%.
    // Can be bypassed by:
    //    Sleep Talk which picks a random move
    //    Snore which is a normal attack
    case Sleep => pk

    // TODO: Non so come gestire lo scongelamento istantaneo
    // Pokémon cannot move while frozen.
    // 20% chance each turn to thaw naturally.
    // Pokémon thaw immediately if hit by a Fire-type move or use certain moves (e.g., Scald, Flame Wheel).
    case Freeze =>
      if scala.util.Random.nextDouble() < FreezeThawChance then
        pk.copy(nonVolatileStatus = Option.empty)
      else
        pk

    // TODO: Non so se gesire qui la cosa del Poison Heal
    // Takes 1/16 max HP damage per turn.
    // Persists indefinitely until cured.
    // Some abilities convert this to a benefit (Poison Heal).
    case Poison =>
      pk.copy(currentHP = pk.currentHP - (pk.base.hp / PoisonedDamageReduction))

    // Damage escalates: 1/16 HP first turn, then increases by 1/16 each turn on the field (e.g., 1/8, 3/16, etc.).
    // Caused only by the Toxic move or its variants (e.g., Toxic Spikes).
    case BadlyPoison =>
      val damage =
        pk.base.hp * (battle.turnIndex - status.turnAssigned) / BadlyPoisonedDamageReduction
      pk.copy(currentHP = pk.currentHP - damage)
    case _ => pk
