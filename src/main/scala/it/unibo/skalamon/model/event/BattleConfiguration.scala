package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.Turn
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.behavior.kind.HealthBehavior
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
        val switches = actions.collect { case SwitchAction() =>
          SwitchAction()
        }
        val moveActions = actions.collect { case MoveAction(m) => m }

        import it.unibo.skalamon.model.move.MoveContext
        implicit val moveOrdering: Ordering[MoveContext] =
          Ordering.by(_.origin.move.priority)
        val sortedMoves = moveActions.sorted

        // TODO: execute all switches

        var newState = t.state.snapshot
        for move <- sortedMoves; (behavior, mod) <- move.behaviors do
          val visitor = BattleStateUpdaterBehaviorVisitor(
            current = newState,
            context = move,
            modifiers = mod
          )
          newState = behavior.accept(visitor)

      case _ => ()
  }
