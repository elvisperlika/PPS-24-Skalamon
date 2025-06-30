package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.battle.Trainer

/** Immutable container for actions picked by trainers (players) during a battle
  * turn.
  */
trait ActionBuffer:
  /** Maximum number of trainers that can register an action in the buffer. */
  val maxSize: Int

  /** Registers an action for a trainer.
    * @param trainer
    *   The trainer registering the action.
    * @param action
    *   The action to register.
    * @return
    *   A new [[ActionBuffer]] instance with the registered action.
    */
  def register(trainer: Trainer, action: Action): ActionBuffer

  /** Withdraws the action registered by a trainer.
    * @param trainer
    *   The trainer whose action is to be withdrawn.
    * @return
    *   A new [[ActionBuffer]] instance without the withdrawn action.
    */
  def withdraw(trainer: Trainer): ActionBuffer

  /** The actions registered by each trainer. */
  def actions: Map[Trainer, Action]

  /** Retrieves the action registered by a trainer.
    * @param trainer
    *   The trainer whose action is to be retrieved.
    * @return
    *   An [[Option]] containing the action if it exists, or None if not.
    */
  def getAction(trainer: Trainer): Option[Action]

  /** Checks if the buffer is full.
    * @return
    *   True if the buffer has reached its maximum size, false otherwise.
    */
  def isFull: Boolean

  /** Clears the action buffer, removing all registered actions.
    * @return
    *   A new empty [[ActionBuffer]] instance.
    */
  def clear(): ActionBuffer

object ActionBuffer:

  /** Creates a new [[ActionBuffer]].
    * @param maxSize
    *   Maximum number of actions that can be registered in the buffer.
    * @return
    *   A new [[ActionBuffer]] instance.
    */
  def apply(maxSize: Int): ActionBuffer = BasicActionBuffer(maxSize)

private case class BasicActionBuffer(
    override val maxSize: Int,
    override val actions: Map[Trainer, Action] = Map.empty
) extends ActionBuffer:

  override def register(trainer: Trainer, action: Action): ActionBuffer =
    if actions.size < maxSize then
      copy(actions = actions + (trainer -> action))
    else this

  override def withdraw(trainer: Trainer): ActionBuffer =
    copy(actions = actions - trainer)

  override def getAction(trainer: Trainer): Option[Action] =
    actions.get(trainer)

  override def isFull: Boolean = actions.size >= maxSize

  override def clear(): ActionBuffer = BasicActionBuffer(maxSize)
