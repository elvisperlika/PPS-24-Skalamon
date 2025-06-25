package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.Turn
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.behavior.visitor.BattleStateUpdaterBehaviorVisitor
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.TurnStageEvents.{Ended, ExecutingActions}

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
    t.state.stage match
      case ActionsReceived(actionBuffer) =>
        val actions = t.state.snapshot.trainers.flatMap(actionBuffer.getAction)
        val switchMoves = actions.collect { case SwitchAction() =>
          SwitchAction()
        }
        val moveActions = actions.collect { case MoveAction(m) => m }

        import it.unibo.skalamon.model.move.MoveContext
        implicit val moveOrdering: Ordering[MoveContext] =
          Ordering.by(_.origin.move.priority)
        val sortedMoveContexts = moveActions.sorted

        // TODO: execute all switches

        // next, execute moves
        var newBattleState = t.state.snapshot
        sortedMoveContexts.foreach { moveContext =>
          val bsv = BattleStateUpdaterBehaviorVisitor(
            current = t.state.snapshot,
            context = ???,
            modifiers = ???
          )
          moveContext.behaviors.foreach { (behavior, modifier) =>
            
          }
        }
      case _ => ()
  }
