package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.Turn
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.behavior.kind.{
  HealthBehavior,
  SingleHitBehavior,
  StatChangeBehavior,
  StatusBehavior
}
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
    executeMoves(t)
  }

  private def executeMoves(turn: Turn): Unit =
    turn.state.stage match
      case ActionsReceived(actionBuffer) =>
        val actions =
          turn.state.snapshot.trainers.flatMap(actionBuffer.getAction)
        val switches = actions.collect { case SwitchAction() => SwitchAction() }
        //TODO: execute switches
        
        val moveContexts = actions.collect { case MoveAction(m) => m }
        import it.unibo.skalamon.model.move.MoveContext
        val orderedPartitions: Map[Int, List[MoveContext]] =
          moveContexts.groupBy(_.origin.move.priority).toSeq.sortBy(_._1).toMap

        orderedPartitions.foreach { (_, moveContexts) =>
          implicit val moveOrdering: Ordering[MoveContext] =
            Ordering.by(_.origin.move.priority)
          val sortedMoveContexts = moveContexts.sorted
          var newState = turn.state.snapshot
          for move <- sortedMoveContexts; (behavior, mod) <- move.behaviors do
            val visitor = BattleStateUpdaterBehaviorVisitor(
              current = newState,
              context = move,
              modifiers = mod
            )
            newState = behavior match
              case b: HealthBehavior     => visitor.visit(b)
              case b: SingleHitBehavior  => visitor.visit(b)
              case b: StatChangeBehavior => visitor.visit(b)
              case b: StatusBehavior     => visitor.visit(b)
        }
      case _ => ()
