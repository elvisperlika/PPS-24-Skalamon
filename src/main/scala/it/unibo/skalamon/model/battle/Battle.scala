package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.Trainer
import it.unibo.skalamon.model.data.Stacks.Stack
import it.unibo.skalamon.model.event.{EventManager, EventType, TurnStageEvents}

/** A battle between trainers.
  * @param trainers
  *   The trainers participating in the battle.
  */
case class Battle(trainers: List[Trainer]):

  /* Stack to maintain battle's turn history */
  private var turnHistory: Stack[Turn] =
    Stack.empty.push(Turn(TurnState.initial(trainers)))

  /** The current turn of the battle. */
  def turn: Option[Turn] = turnHistory.peek

  /** The event manager for handling battle/turn events. */
  val eventManager = EventManager()

  /** Makes the battle advance to the next stage.
    */
  def update(): Unit =
    turn match
      case Some(t) => update(t)
      case _ => throw new IllegalStateException("No active turn to update")

  private def update(turn: Turn): Unit =
    import TurnStage.*
    given Turn = turn
    turn.state.stage match
      case Started                       => setStage(WaitingForActions)
      case WaitingForActions             => return
      case ActionsReceived(actionBuffer) => setStage(ExecutingActions)
      case ExecutingActions              => setStage(Ended)
      case Ended => turnHistory = turnHistory.push(Turn(turn.state.copy(stage = Started)))

    given Conversion[TurnStage, EventType[Turn]] = TurnStageEvents.from(_)

    eventManager.notify(turn.state.stage of turn)

  private def setStage(stage: TurnStage)(using turn: Turn): Unit =
    turn.state = turn.state.copy(stage = stage)
