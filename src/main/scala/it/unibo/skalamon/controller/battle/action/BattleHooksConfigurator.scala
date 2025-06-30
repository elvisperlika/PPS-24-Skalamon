package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Turn,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.event.TurnStageEvents.{ActionsReceived, Started}

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.hookBattleStateUpdate(ActionsReceived) { (state, turn) =>
      println("EXECUTING ACTIONS\nx\nx")
      executeMoves(turn)
    }

    battle.hookBattleStateUpdate(Started) { (state, _) =>
      updateBattleField(state)
    }

    def updateBattleField(battleState: BattleState): BattleState =
      import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredEffects
      battleState.copy(
        field = battleState.field.removeExpiredEffects(battle.turnIndex)
      )

    def executeMoves(turn: Turn): BattleState =
      var state: BattleState = turn.state.snapshot
      import it.unibo.skalamon.model.battle.TurnStage
      turn.state.stage match
        case TurnStage.ActionsReceived(actionBuffer) =>
          import it.unibo.skalamon.model.event.config.OrderingUtils.given
          val sortedActions = actionBuffer.actions.values.toSeq.sorted

          sortedActions.foreach {
            case MoveAction(context) => state = context(state)
            case SwitchAction()      => () // TODO
            case _                   => ()
          }
        case _ => throw new IllegalStateException(
            s"Cannot execute moves in stage ${turn.state.stage}"
          )
      state
