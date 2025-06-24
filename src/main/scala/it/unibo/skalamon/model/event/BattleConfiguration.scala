package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.TurnStageEvents.TurnEnded

trait BattleConfiguration extends EventManager:
  watch(TurnEnded) { t =>
    val aliveTrainers =
      t.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
    aliveTrainers match
      case Seq(winner) =>
        notify(Finished of Some(winner))
      case Seq() =>
        notify(Finished of None)
      case _ => (false, None)
  }
