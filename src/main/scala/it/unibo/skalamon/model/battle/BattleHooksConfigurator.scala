package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.controller.battle.action.Action
import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.controller.battle.action.SwitchAction
import it.unibo.skalamon.model.ability.hookAll
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.battle.turn.BattleEvents._
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.behavior.notifyFieldEffects
import it.unibo.skalamon.model.event.ActionEvents
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.event.TurnStageEvents.ActionsReceived
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.event.TurnStageEvents.Started
import it.unibo.skalamon.model.field.Field
import it.unibo.skalamon.model.field.FieldEffectMixin._
import it.unibo.skalamon.model.field.PokemonRule
import it.unibo.skalamon.model.move._
import it.unibo.skalamon.model.pokemon.BattlePokemon

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

    battle.eventManager.watch(Started) { t =>
    }

    battle.eventManager.watch(Ended) { t =>
      checkGameOver(t)
    }

    def checkGameOver(turn: Turn): Unit =
      val aliveTrainers =
        turn.state.snapshot.trainers.filter(_.team.exists(_.isAlive))
      aliveTrainers match
        case List(winner) => battle.gameState = GameOver(Some(winner))
        case Nil          => battle.gameState = GameOver(None)
        case _            =>

    battle.eventManager.watch(CreateWeather) {
      case t: Weather with PokemonRules => hookWeatherEffects(t)
      case _                            =>
    }

    battle.eventManager.watch(CreateTerrain) {
      case t: Terrain with PokemonRules => hookTerrainEffects(t)
      case _                            =>
    }

    battle.eventManager.watch(CreateRoom) {
      case r: Room with PokemonRules      => hookRoomEffects(r)
      case r: Room with MutatedBattleRule => hookBattleRules(r)
      case _                              =>
    }

    battle.eventManager.watch(ActionsReceived) { turn =>
      println("EXECUTING ACTIONS\nx\nx")
      executeActions(turn)
    }

    battle.hookBattleStateUpdate(ActionEvents.Move) { (state, action) =>
      val updSource: Trainer = state.trainers.find(_.id == action.source.id).get
      val updTarget: Trainer = state.trainers.find(_.id == action.target.id).get
      executeMove(action.move, updSource, updTarget, state)
    }

    battle.hookBattleStateUpdate(ActionEvents.Switch) { (state, action) =>
      executeSwitch(action.in, state)
    }

    battle.hookBattleStateUpdate(Ended) { (state, _) =>
      updateBattlefield(state)
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

    def updateTeam(trainers: List[Trainer], rule: PokemonRule): List[Trainer] =
      trainers.map(t =>
        val updTeam = t.team.map(p =>
          if t.inField.get is p then rule(p) else p
        )
        t.copy(team = updTeam)
      )

    def hookWeatherEffects[T <: Weather with PokemonRules](o: T): Unit =
      o.rules.foreach: pokemonRule =>
        val (event, rule) = pokemonRule
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.weather match
            case Some(`o`) =>
              state.copy(trainers = updateTeam(state.trainers, rule))
            case _ => state
        }

    def hookTerrainEffects[T <: Terrain with PokemonRules](o: T): Unit =
      o.rules.foreach: pokemonRule =>
        val (event, rule) = pokemonRule
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.terrain match
            case Some(`o`) =>
              state.copy(trainers = updateTeam(state.trainers, rule))
            case _ => state
        }

    def hookRoomEffects[T <: Room with PokemonRules](o: T): Unit =
      o.rules.foreach: pokemonRule =>
        val (event, rule) = pokemonRule
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.room match
            case Some(`o`) =>
              state.copy(trainers = updateTeam(state.trainers, rule))
            case _ => state
        }

    def hookBattleRules[T <: Room with MutatedBattleRule](o: T): Unit =
      battle.hookBattleStateUpdate(Started) { (state, t) =>
        state.field.room match
          case Some(`o`) => state.copy(rules = o.rule)
          case _         => state.copy(rules = battle.rules)
      }

    def updateBattlefield(state: BattleState): BattleState =
      import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredEffects
      state.copy(
        field = state.field.removeExpiredEffects(battle.turnIndex)
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
      val result: (Move => MoveContext => Behavior, EventType[MoveContext]) =
        move.move.accuracy match
          case _ if move.pp <= 0 => (_ => move.move.fail, Miss)
          case MoveModel.Accuracy.Of(percentage)
              if !percentage.randomBoolean => (_ => move.move.fail, Miss)
          case _ => (_ => move.move.success, Hit)
      val (behavior, successEvent) = result
      val context =
        move.createContext(behavior, target.inField.get, source.inField.get)
      behavior(move.move)(context).notifyFieldEffects(battle)
      battle.eventManager.notify(successEvent of context)
      val newState = context(current)
      context.decrementPP(newState)

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
