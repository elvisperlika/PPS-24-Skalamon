package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState
import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.model.data.Stacks.Stack
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.{
  BattleConfiguration,
  EventManager,
  EventType,
  TurnStageEvents
}

/** A battle between trainers.
  * @param trainers
  *   The trainers participating in the battle.
  */
case class Battle(trainers: List[Trainer]):

  var gameState: GameState = InProgress

  /** Stack to maintain battle's turn history
    */
  private var turnHistory: Stack[Turn] = Stack.empty

  /** The current turn of the battle.
    */
  def currentTurn: Option[Turn] = turnHistory.peek

  /** The event manager for handling battle/turn events.
    */
  val battleEventManager: EventManager =
    new EventManager with BattleConfiguration

  battleEventManager.watch(Finished) { maybeWinner =>
    gameState = GameOver(maybeWinner)
  }

  /** Starts the battle by initializing the first turn.
    */
  def start(): Unit =
    turnHistory = turnHistory push Turn(TurnState.initial(trainers))
    setStage(TurnStage.Started)

  /** Makes the battle advance to the next stage.
    */
  def update(): Unit =
    currentTurn match
      case Some(t) => update(t)
      case _ => throw new IllegalStateException("No active turn to update")

  private def update(turn: Turn): Unit =
    import TurnStage.*
    turn.state.stage match
      case Started                       => setStage(WaitingForActions)
      case WaitingForActions             =>
      case ActionsReceived(actionBuffer) => setStage(ExecutingActions)
      case ExecutingActions              => setStage(Ended)
      case Ended                         =>
        turnHistory = turnHistory push Turn(turn.state.copy(stage = Started))

  /** Sets the stage of the current turn, and notifies the event manager of the
    * change via the appropriate [[TurnStageEvents]].
    *
    * @param stage
    *   The new stage to set for the turn.
    */
  def setStage(stage: TurnStage): Unit =
    currentTurn match
      case Some(turn) =>
        turn.state = turn.state.copy(stage = stage)
        given Conversion[TurnStage, EventType[Turn]] = TurnStageEvents.from(_)
        battleEventManager.notify(turn.state.stage of turn)
      case None =>
        throw new IllegalStateException("No active turn to set stage")
