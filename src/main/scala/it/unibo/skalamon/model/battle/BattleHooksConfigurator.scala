package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.TurnStageEvents.{ActionsReceived, Started}
import it.unibo.skalamon.model.move.{Move, MoveModel, createContext}
import it.unibo.skalamon.model.ability.hookAll

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.hookBattleStateUpdate(ActionsReceived) { (state, turn) =>
      println("EXECUTING ACTIONS\nx\nx")
      executeMoves(turn)
    }

    battle.hookBattleStateUpdate(Started) { (state, _) =>
      updateBattleField(state)
    }

    battle.trainers.foreach { trainer =>
      trainer.team.foreach { pokemon =>
        pokemon.base.ability.hookAll(battle)(
          source = Some(pokemon).filter(trainer.inField.contains),
          target = battle.trainers.find(_ != trainer).flatMap(_.inField)
        )
      }
    }

    def updateBattleField(battleState: BattleState): BattleState =
      import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredEffects
      battleState.copy(
        field = battleState.field.removeExpiredEffects(battle.turnIndex)
      )

    def executeMoves(turn: Turn): BattleState =
      val initialState = turn.state.snapshot
      turn.state.stage match
        case TurnStage.ActionsReceived(actionBuffer) =>
          import it.unibo.skalamon.model.event.config.OrderingUtils.given
          val sortedActions = actionBuffer.actions.values.toList.sorted
          val finalState = sortedActions.foldLeft(initialState) { (s, a) =>
            a match
              case MoveAction(move, source, target) =>
                val result: Move => Behavior = _ =>
                  move.move.accuracy match
                    case MoveModel.Accuracy.Of(percentage)
                        if !percentage.randomBoolean => move.move.fail
                    case _ => move.move.success
                move.createContext(result, target, source)(s)
              case SwitchAction(pIn, pOut) => s
              case _                       => s
          }
          finalState

        case _ =>
          initialState
