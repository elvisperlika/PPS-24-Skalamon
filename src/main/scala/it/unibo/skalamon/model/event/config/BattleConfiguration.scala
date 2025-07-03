package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.model.battle.{Battle, Turn}
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.event.TurnStageEvents.Ended

/** Mixin of [[EventManager]] for events pre-configuration.
  */
trait BattleConfiguration(battle: Battle) extends EventManager:
  watch(Ended) { t =>
    checkGameOver(t)
  }

  private def checkGameOver(turn: Turn): Unit =
    val aliveTrainers =
      turn.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
    aliveTrainers match
      case List(winner) => battle.gameState = GameOver(Some(winner))
      case Nil          => battle.gameState = GameOver(None)
      case _            =>
