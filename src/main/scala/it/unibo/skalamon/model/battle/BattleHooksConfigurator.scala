package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.ability.hookAll
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.battle.turn.BattleEvents.{
  Hit,
  Miss,
  PokemonSwitchIn,
  PokemonSwitchOut
}
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.event.TurnStageEvents.{ActionsReceived, Started}
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.BattlePokemon

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.hookBattleStateUpdate(ActionsReceived) { (state, turn) =>
      println("EXECUTING ACTIONS\nx\nx")
      executeMoves(turn)
    }

    battle.hookBattleStateUpdate(Started) { (state, _) =>
      updateBattleField(state)
    }

    battle.trainers.zipWithIndex.foreach { (trainer, trainerIndex) =>
      trainer.team.foreach { pokemon =>
        val state = () => battle.currentTurn.map(_.state.snapshot).get
        val sourceTrainer = () => state().trainers(trainerIndex)
        val targetTrainer =
          () => state().trainers.find(_ != sourceTrainer()).get

        pokemon.base.ability.hookAll(battle)(
          source =
            for
              field <- sourceTrainer().inField
              if pokemon is field
            yield pokemon,
          target = targetTrainer().inField
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
                val updSource: Trainer = s.trainers.find(_.id == source.id).get
                val updTarget: Trainer = s.trainers.find(_.id == target.id).get
                executeMove(move, updSource, updTarget, s)
              case SwitchAction(pIn) => executeSwitch(pIn, s)
              case _                 => s
          }
          finalState
        case _ =>
          initialState

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
