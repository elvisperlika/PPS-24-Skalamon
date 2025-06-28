package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState
import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.model.data.Stacks.Stack
import it.unibo.skalamon.model.event.*
import it.unibo.skalamon.model.event.BattleStateEvents.Finished
import it.unibo.skalamon.model.event.config.BattleConfiguration

/** A battle between trainers.
  * @param trainers
  *   The trainers participating in the battle.
  */
case class Battle(trainers: List[Trainer]) extends EventManagerProvider:

  var gameState: GameState = InProgress

  /** Stack to maintain battle's turn history
    */
  private var turnHistory: Stack[Turn] = Stack.empty

  var turnIndex: Int = turnHistory.size

  /** The current turn of the battle.
    */
  def currentTurn: Option[Turn] = turnHistory.peek

  /** The event manager for handling battle/turn events.
    */
  override val eventManager: EventManager =
    new EventManager with BattleConfiguration(this)

  eventManager.watch(Finished) { maybeWinner =>
    gameState = GameOver(maybeWinner)
  }

  /** Starts the battle by initializing the first turn.
    */
  def start(): Unit =
    given turn: Turn = Turn(TurnState.initial(trainers))
    turnHistory = turnHistory push turn
    setStage(TurnStage.Started)

  /** Notifies each event from the current turn's [[BattleState]]'s event queue
    * to the event manager, and updates the turn state making the battle advance
    * to the next stage.
    * @throws IllegalStateException
    *   If there is no active turn to update.
    */
  def update(): Unit =
    currentTurn match
      case Some(t) => update(t)
      case _ => throw new IllegalStateException("No active turn to update")

  private def update(turn: Turn): Unit =
    import TurnStage.*
    given Turn = turn
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
  def setStage(stage: TurnStage)(using turn: Turn): Unit =
    given Conversion[TurnStage, EventType[Turn]] = TurnStageEvents.from(_)
    turn.state = turn.state.copy(stage = stage)
    eventManager.notify(turn.state.stage of turn)
