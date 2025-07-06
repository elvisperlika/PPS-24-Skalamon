package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState
import it.unibo.skalamon.controller.battle.action.{
  Action,
  MoveAction,
  SwitchAction
}
import it.unibo.skalamon.model.status.AssignedStatus
import it.unibo.skalamon.model.ability.hookAll
import it.unibo.skalamon.model.battle.hookBattleStateUpdate
import it.unibo.skalamon.model.battle.turn.BattleEvents.*
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.TurnStageEvents.{ActionsReceived, Started}
import it.unibo.skalamon.model.event.{
  ActionEvents,
  BattleStateEvents,
  EventType
}
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  MutatedBattleRule
}
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
      val result: (Move => MoveContext => Behavior, EventType[MoveContext]) =
        move.move.accuracy match
          case _ if move.pp <= 0 => (_ => move.move.fail, Miss)
          case MoveModel.Accuracy.Of(percentage)
              if !percentage.randomBoolean => (_ => move.move.fail, Miss)
          case _ => (_ => move.move.success, Hit)

      val context =
        move.createContext(result._1, target.inField.get, source.inField.get)

      battle.eventManager.notify(result._2 of context)
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

    /** Removes expired volatile statuses from a Pokémon.
      *
      * @param pk
      *   The Pokémon from which to remove expired statuses.
      * @return
      *   The Pokémon with expired statuses removed.
      */
    def removeExpiredStatuses(pk: BattlePokemon): BattlePokemon =
      val updatedVolatileStatuses = pk.volatileStatus.filterNot {
        case AssignedStatus(status: Expirable, _) =>
          status.isExpired(battle.turnIndex)
        case _ => false
      }
      pk.copy(volatileStatus = updatedVolatileStatuses)
