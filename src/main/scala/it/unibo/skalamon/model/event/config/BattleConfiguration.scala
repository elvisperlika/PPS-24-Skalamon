package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.Turn
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.event.BattleStateEvents.{Changed, Finished}
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.event.TurnStageEvents.{Ended, ExecutingActions}
import it.unibo.skalamon.model.event.config.OrderingUtils

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

  watch(ExecutingActions) { t =>
    executeMoves(t)
  }

  private def executeMoves(turn: Turn): Unit =
    turn.state.stage match
      case ActionsReceived(actionBuffer) =>
        import OrderingUtils.given
        val sortedActions =
          turn.state.snapshot.trainers.map(actionBuffer.getAction).collect {
            case Some(m) => m
          }.sorted
        val oldState = turn.state.snapshot
        sortedActions.foreach {
          case MoveAction(move) =>
            val newState = move(oldState)
            notify(Changed of (oldState, newState))
          case SwitchAction() => () // TODO
          case _              => ()
        }
      case _ => ()
