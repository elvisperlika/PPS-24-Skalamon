package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.status.Status

/** Behavior that applies a status to a target Pokémon.
  * @param status
  *   The status to be applied
  * @param currentTurnIndex
  *   The index of the current turn in which the status is applied
  */
case class StatusBehavior(status: Status, currentTurnIndex: Int)
    extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
