package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.Trainer
import it.unibo.skalamon.controller.battle.action.ActionBuffer
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** A turn in a battle.
  *
  * @param state
  *   The current state of the turn.
  */
case class Turn(var state: TurnState)

/** A snapshot of a turn state in a battle.
  *
  * @param pokemonInField
  *   A map of trainers to their Pokémon currently in the field.
  * @param stage
  *   The current stage of the turn.
  */
case class TurnState(
    pokemonInField: Map[Trainer, BattlePokemon],
    stage: TurnStage
)

/** The different stages a turn can be in. */
enum TurnStage:
  /** The turn has just started.
    *
    * Here, abilities can be activated and weather effects can be applied.
    */
  case Started

  /** Waiting for trainers to register their actions.
    *
    * Here, trainers can register their actions for the turn.
    */
  case WaitingForActions

  /** Actions have been received from all trainers.
    *
    * Here, the actions are collected in an [[ActionBuffer]] and the turn is
    * ready to execute them.
    *
    * @param actionBuffer
    *   The buffer containing the actions registered by trainers.
    */
  case ActionsReceived(actionBuffer: ActionBuffer)

  /** Actions are being executed.
    *
    * Here, the actions registered by trainers, anwith their behaviors, are
    * executed according to priority and speed.
    */
  case ExecutingActions

  /** The turn has ended and is ready for the next one.
    *
    * Here, abilities can be activated, and expirable effects can decrease.
    */
  case Ended

object TurnState:
  /** Initializes a new turn state with the given trainers.
    *
    * @param trainers
    *   The list of trainers participating in the turn.
    * @return
    *   A new TurnState with the initial Pokémon in the field and the stage set
    *   to Started.
    */
  def initial(trainers: List[Trainer]): TurnState =
    TurnState(
      pokemonInField =
        trainers.map(trainer => trainer -> trainer.team.head).toMap,
      stage = TurnStage.Started
    )
