package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.{
  Action,
  ActionBuffer,
  MoveAction,
  SwitchAction
}
import it.unibo.skalamon.model.battle.TurnStage.ActionsReceived
import it.unibo.skalamon.model.battle.{Battle, Trainer, Turn, TurnStage}
import it.unibo.skalamon.model.event.TurnStageEvents.ExecutingActions
import it.unibo.skalamon.model.event.config.OrderingUtils
import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Turn,
  hookBattleStateUpdate
}

/** Controller for managing battles in the game.
  *
  * This controller handles the registration of actions from trainers and
  * updates the battle state.
  */
trait BattleController:
  /** The battle managed by this controller. */
  val battle: Battle

  /** Starts the battle.
    *
    * This method initializes the battle and sets the initial turn stage.
    */
  def start(): Unit

  /** Registers an action from a trainer.
    *
    * @param trainer
    *   The trainer who is performing the action.
    * @param action
    *   The action to be registered.
    * @throws IllegalStateException
    *   If the current turn stage does not allow action registration.
    */
  def registerAction(trainer: Trainer, action: Action): Unit

  /** Updates the battle state to the next stage.
    */
  def update(): Unit

object BattleController:
  /** Creates a new instance of BattleController for the given battle.
    *
    * @param battle
    *   The battle to be managed by the controller.
    * @return
    *   A new BattleController instance.
    */
  def apply(battle: Battle): BattleController =
    new BattleControllerImpl(battle)

private class BattleControllerImpl(override val battle: Battle)
    extends BattleController:

  battle.hookBattleStateUpdate(ExecutingActions) { (battleState, turn) =>
    executeMoves(turn)
  }

  private def executeMoves(turn: Turn): BattleState =
    var state: BattleState = turn.state.snapshot
    turn.state.stage match
      case ActionsReceived(actionBuffer) =>
        import OrderingUtils.given
        val sortedActions =
          turn.state.snapshot.trainers.map(actionBuffer.getAction).collect {
            case Some(m) => m
          }.sorted
        sortedActions.foreach {
          case MoveAction(context) => state = context(state)
          case SwitchAction()      => () // TODO
          case _                   => ()
        }
      case _ => ()
    state

  private var actionBuffer = ActionBuffer(battle.trainers.size)

  override def start(): Unit = battle.start()

  override def registerAction(trainer: Trainer, action: Action): Unit =
    import TurnStage.*

    battle.currentTurn match
      case Some(turn) if turn.state.stage == WaitingForActions =>
        this.actionBuffer = actionBuffer.register(trainer, action)
        if actionBuffer.isFull then
          given Turn = turn
          battle.setStage(ActionsReceived(actionBuffer))
          this.actionBuffer = actionBuffer.clear()
      case _ => throw new IllegalStateException(
          "Cannot accept actions in the current turn stage."
        )

  override def update(): Unit = battle.update()
