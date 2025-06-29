package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.TurnStageEvents.{ExecutingActions, Started}
import it.unibo.skalamon.model.move.{Move, MoveModel}
import it.unibo.skalamon.model.move.createContext

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
      val initialState = turn.state.snapshot
      turn.state.stage match
        case ActionsReceived(actionBuffer) =>
          import it.unibo.skalamon.model.event.config.OrderingUtils.given
          val sortedActions =
            initialState.trainers.flatMap(actionBuffer.getAction).sorted

          val finalState = sortedActions.foldLeft(initialState) { (s, a) =>
            a match
              case MoveAction(battleMove, source, target) =>
                val result: Move => Behavior = _ =>
                  battleMove.move.accuracy match
                    case MoveModel.Accuracy.Of(percentage)
                        if !percentage.randomBoolean => battleMove.move.fail
                    case _ => battleMove.move.success

                battleMove.createContext(result, target, source)(initialState)
              case SwitchAction(pIn, pOut) => s
              case _                       => s
          }
          finalState

        case _ =>
          initialState
