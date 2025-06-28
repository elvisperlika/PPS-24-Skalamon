package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Turn,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.event.TurnStageEvents.{ExecutingActions, Started}

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.hookBattleStateUpdate(ExecutingActions) { (state, turn) =>
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
      turn.state.stage match
        case ActionsReceived(actionBuffer) =>
          import it.unibo.skalamon.model.event.config.OrderingUtils.given
          val sortedActions =
            turn.state.snapshot.trainers.map(actionBuffer.getAction).collect {
              case Some(m) => m
            }.sorted
          sortedActions.foreach {
            case MoveAction(context) => state = context(state)
            case SwitchAction()      => () // TODO
            case _                   => ()
          }
        case _ => ()
      state
