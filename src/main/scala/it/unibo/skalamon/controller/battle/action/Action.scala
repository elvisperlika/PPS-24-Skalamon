package it.unibo.skalamon.controller.battle.action

trait Action

case class MoveAction() extends Action

case class SwitchAction() extends Action
