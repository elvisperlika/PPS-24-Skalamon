package it.unibo.skalamon.model.move

trait Action

case class Move() extends Action

case class Switch() extends Action
