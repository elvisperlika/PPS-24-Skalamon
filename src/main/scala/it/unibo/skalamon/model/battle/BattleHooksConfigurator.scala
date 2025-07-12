package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState
import it.unibo.skalamon.controller.battle.GameState.GameOver
import it.unibo.skalamon.controller.battle.action.{
  Action,
  MoveAction,
  SwitchAction
}
import it.unibo.skalamon.model.status.AssignedStatus
import it.unibo.skalamon.model.ability.hookAll
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.battle.turn.BattleEvents.*
import it.unibo.skalamon.model.behavior.{Behavior, notifyFieldEffects}
import it.unibo.skalamon.model.event.{
  ActionEvents,
  BattleStateEvents,
  EventType
}
import it.unibo.skalamon.model.event.TurnStageEvents.{
  ActionsReceived,
  Ended,
  Started
}
import it.unibo.skalamon.model.field.FieldEffectMixin.*
import it.unibo.skalamon.model.field.{Field, FieldEffectMixin, PokemonRule}
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.BattlePokemon

object BattleHooksConfigurator:

  def configure(battle: Battle): Unit =

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
      case t: Weather with Hooks => hookWeatherEffects(t)
      case _                     =>
    }

    battle.eventManager.watch(CreateTerrain) {
      case t: Terrain with Hooks => hookTerrainEffects(t)
      case _                     =>
    }

    battle.eventManager.watch(CreateRoom) {
      case r: Room with Hooks             => hookRoomEffects(r)
      case r: Room with MutatedBattleRule => hookBattleRules(r)
      case _                              =>
    }

    battle.eventManager.watch(ActionsReceived) { turn =>
      executeActions(turn)
    }

    battle.hookBattleStateUpdate(ActionEvents.Move) { (state, action) =>
      val updSource: Trainer = state.trainers.find(_.id == action.source.id).get
      val updTarget: Trainer = state.trainers.find(_.id == action.target.id).get
      executeMove(action.move, updSource, updTarget, state)
    }

    battle.hookBattleStateUpdate(ActionEvents.Switch) { (state, action) =>
      val trainer =
        state.trainers.find(_.team.exists(_.id == action.in.id)).head
      val removedVolatileStatuses = removeVolatileStatuses(trainer, state)
      executeSwitch(action.in, removedVolatileStatuses)
    }

    battle.hookBattleStateUpdate(Ended) { (state, _) =>
      updateBattlefield(state)
    }

    battle.hookBattleStateUpdate(Ended) { (state, _) =>
      executeStatus(state)
    }

    battle.hookBattleStateUpdateOption(BattleStateEvents.Changed) {
      (state, _) =>
        val koTrainers =
          state.trainers.filter(t => !t.inField.exists(_.isAlive))
        if battle.gameState == GameState.InProgress && koTrainers.nonEmpty then
          val updatedTrainers = state.trainers.map { trainer =>
            if koTrainers.contains(trainer) then
              trainer.team.find(_.isAlive) match
                case pokemon @ Some(_) => trainer.copy(_inField = pokemon)
                case _                 =>
                  val winner = state.trainers.find(_ != trainer)
                  battle.gameState = GameState.GameOver(winner)
                  battle.eventManager.notify(
                    BattleStateEvents.Finished of winner
                  )
                  trainer
            else
              trainer
          }
          Some(state.copy(trainers = updatedTrainers))
        else
          None
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

    def hookWeatherEffects[T <: Weather with Hooks](o: T): Unit =
      o.hooks.foreach: pokemonRule =>
        val (event, rule) = pokemonRule
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.weather match
            case Some(`o`) =>
              state.copy(trainers = updateTeam(state.trainers, rule))
            case _ => state
        }

    def hookTerrainEffects[T <: Terrain with Hooks](o: T): Unit =
      o.hooks.foreach: pokemonRule =>
        val (event, rule) = pokemonRule
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.terrain match
            case Some(`o`) =>
              state.copy(trainers = updateTeam(state.trainers, rule))
            case _ => state
        }

    def hookRoomEffects[T <: Room with Hooks](o: T): Unit =
      o.hooks.foreach: pokemonRule =>
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

    /** Executes the status effects for each Pokémon in the battle state. This
      * method is called at the end of each turn to apply non-volatile and
      * volatile status effects.
      *
      * @param bt
      *   The current battle state containing all trainers and their Pokémon.
      * @return
      *   The updated battle state with applied status effects.
      */
    def executeStatus(bt: BattleState): BattleState =
      val updatedTrainers = bt.trainers.map { trainer =>
        trainer.inField match
          case Some(inFieldPokemon) =>
            val original = trainer.team.find(_.id == inFieldPokemon.id).get
            val resetFlags =
              original.copy(isProtected = false, skipsCurrentTurn = false)

            val afterNonVolatile = resetFlags.nonVolatileStatus match
              case Some(assignedStatus) =>
                assignedStatus.status.executeEffect(resetFlags)
              case None => resetFlags

            val afterVolatile =
              afterNonVolatile.volatileStatus.foldLeft(afterNonVolatile) {
                (current, assignedStatus) =>
                  assignedStatus.status.executeEffect(
                    current,
                    battle.turnIndex,
                    assignedStatus.turnAssigned
                  )
              }

            val cleaned = removeExpiredStatuses(afterVolatile)

            val updatedTeam = trainer.team.map {
              case p if p.id == cleaned.id => cleaned
              case p                       => p
            }

            trainer.copy(team = updatedTeam)

          case None => trainer
      }

      bt.copy(trainers = updatedTrainers)

    /** Removes volatile statuses from the Pokémon in the trainer's team that is
      * @param trainer
      *   The trainer whose Pokémon's volatile statuses will be removed.
      * @param state
      *   The current battle state containing all trainers and their Pokémon.
      * @return
      *   The updated battle state with the trainer's Pokémon having no
      */
    def removeVolatileStatuses(
        trainer: Trainer,
        state: BattleState
    ): BattleState =
      val updatedTeam = trainer.team.map: pokemon =>
        if pokemon.id == trainer.inField.get.id then
          pokemon.copy(volatileStatus = Set.empty)
        else
          pokemon

      state.copy(
        trainers = state.trainers.map:
          case t if t.id == trainer.id => trainer.copy(team = updatedTeam)
          case t                       => t
      )

    /** Removes expired volatile statuses from a Pokémon.
      *
      * @param pk
      *   The Pokémon from which to remove expired statuses.
      * @return
      *   The Pokémon with expired statuses removed.
      */
    def removeExpiredStatuses(pk: BattlePokemon): BattlePokemon =
      import it.unibo.skalamon.model.battle.ExpirableSystem.removeExpiredVolatileStatuses
      pk.copy(volatileStatus =
        pk.volatileStatus.removeExpiredVolatileStatuses(battle.turnIndex)
      )
