package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.move.MoveContext

trait Action

case class MoveAction(move: MoveContext) extends Action

case class SwitchAction() extends Action
