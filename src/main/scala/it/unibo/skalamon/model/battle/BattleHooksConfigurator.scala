package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.{
  Action,
  MoveAction,
  SwitchAction
}
import it.unibo.skalamon.model.ability.hookAll
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.battle.turn.BattleEvents.*
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.TurnStageEvents.{ActionsReceived, Started}
import it.unibo.skalamon.model.event.{ActionEvents, EventType}
import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.MutatedBattleRule
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.BattlePokemon

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.eventManager.watch(ActionsReceived) { turn =>
      println("EXECUTING ACTIONS\nx\nx")
      executeActions(turn)
    }

    battle.hookBattleStateUpdate(ExpiredRoom) { (state, room) =>
      state.copy(rules = battle.rules)
    }

    battle.hookBattleStateUpdate(CreateRoom) { (state, room) =>
      room match
        case r: FieldEffectMixin.Room with MutatedBattleRule =>
          state.copy(rules = r.rule)
        case _ => state
    }

    battle.hookBattleStateUpdate(ActionEvents.Move) { (state, action) =>
      val updSource: Trainer = state.trainers.find(_.id == action.source.id).get
      val updTarget: Trainer = state.trainers.find(_.id == action.target.id).get
      executeMove(action.move, updSource, updTarget, state)
    }

    battle.hookBattleStateUpdate(ActionEvents.Switch) { (state, action) =>
      executeSwitch(action.in, state)
    }

    battle.hookBattleStateUpdate(Started) { (state, _) =>
      updateBattleField(state)
    }

    battle.trainers.zipWithIndex.foreach { (trainer, trainerIndex) =>
      trainer.team.foreach { pokemon =>
        val sourceTrainer = (state: BattleState) => state.trainers(trainerIndex)
        val targetTrainer =
          (state: BattleState) => state.trainers.find(_ != sourceTrainer(state))

        pokemon.base.ability.hookAll(battle)(
          source = state =>
            for inField <- sourceTrainer(state).inField
            // if pokemon is inField // TODO problem with non-updated state
            yield pokemon,
          target = targetTrainer(_).flatMap(_.inField)
        )
      }
    }

    def updateBattleField(battleState: BattleState): BattleState =
      import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredEffects
      battleState.copy(
        field = battleState.field.removeExpiredEffects(battle.turnIndex)
      )

    def executeActions(turn: Turn): Unit =
      turn.state.stage match
        case TurnStage.ActionsReceived(actionBuffer) =>
          val orderStrategy: Ordering[Action] =
            turn.state.snapshot.rules.actionOrderStrategy
          val sortedActions =
            actionBuffer.actions.values.toList.sorted(orderStrategy)
          sortedActions.foreach {
            case action @ MoveAction(_, _, _) =>
              battle.eventManager.notify(ActionEvents.Move of action)
            case action @ SwitchAction(_) =>
              battle.eventManager.notify(ActionEvents.Switch of action)
          }
        case _ =>

    def executeMove(
        move: BattleMove,
        source: Trainer,
        target: Trainer,
        current: BattleState
    ): BattleState =
      val result: (Move => Behavior, EventType[MoveContext]) =
        move.move.accuracy match
          case MoveModel.Accuracy.Of(percentage)
              if !percentage.randomBoolean => (_ => move.move.fail, Miss)
          case _ => (_ => move.move.success, Hit)
      val context =
        move.createContext(result._1, target.inField.get, source.inField.get)
      battle.eventManager.notify(result._2 of context)
      context(current)

    def executeSwitch(
        pIn: BattlePokemon,
        state: BattleState
    ): BattleState =
      val owner =
        state.trainers.find(_.team.exists(_.id == pIn.id)).get
      owner.inField match
        case Some(p) => battle.eventManager.notify(PokemonSwitchOut of p)
        case _       => ()
      val updatedTrainers =
        state.trainers.map {
          case `owner`    => owner.copy(_inField = Some(pIn))
          case t: Trainer => t
        }
      battle.eventManager.notify(PokemonSwitchIn of pIn)
      state.copy(trainers = updatedTrainers)
