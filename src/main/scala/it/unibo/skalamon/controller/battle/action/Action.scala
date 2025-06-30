package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.move.MoveContext

trait Action:
  val priority: Int

case class MoveAction(context: MoveContext) extends Action:
  override val priority: Int = context.origin.move.priority

case class SwitchAction() extends Action:
  override val priority: Int = SwitchAction.Priority

object SwitchAction:
  private val Priority: Int = 6
