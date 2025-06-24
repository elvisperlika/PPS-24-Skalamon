package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.battle.Turn
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.TurnStageEvents.Ended

trait BattleConfiguration extends EventManager:
  watch(Ended) { t =>
    checkGameOver(t)
  }

  private def checkGameOver(turn: Turn): Unit =
    val aliveTrainers =
      turn.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
    aliveTrainers match
      case Seq(winner) => notify(Finished of Some(winner))
      case Seq()       => notify(Finished of None)
      case _           => ()
