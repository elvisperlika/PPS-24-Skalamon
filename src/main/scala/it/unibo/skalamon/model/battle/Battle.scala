package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.Trainer
import it.unibo.skalamon.model.event.{EventManager, EventType, TurnStageEvents}

/** A battle between trainers.
  * @param trainers
  *   The trainers participating in the battle.
  */
case class Battle(trainers: List[Trainer]):
  // TODO use stack
  private var _turn = Turn(TurnState.initial(trainers))

  /** The current turn of the battle. */
  def turn: Turn = _turn

  /** The event manager for handling battle/turn events. */
  val eventManager = EventManager()

  /** Makes the battle advance to the next stage.
    */
  def update(): Unit =
    import TurnStage.*
    turn.state.stage match
      case Started =>
        turn.state = turn.state.copy(stage = TurnStage.WaitingForActions)

      case WaitingForActions => return

      case ActionsReceived(actionBuffer) =>
        turn.state = turn.state.copy(stage = TurnStage.ExecutingActions)

      case TurnStage.ExecutingActions =>
        turn.state = turn.state.copy(stage = TurnStage.Ended)

      case TurnStage.Ended =>
        // TODO handle stack
        _turn = Turn(turn.state.copy(stage = TurnStage.Started))

    given Conversion[TurnStage, EventType[Turn]] = TurnStageEvents.from(_)
    eventManager.notify(turn.state.stage of turn)
