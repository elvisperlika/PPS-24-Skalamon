package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.status.Status

/** Behavior that applies a status to a target Pokémon.
  * @param status
  *   The status to be applied, with the current turn index as a parameter.
  */
case class StatusBehavior(status: (turnIndex: Int) => Status)
    extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)

/** Behavior that clears all statuses from a target Pokémon.
  */
class ClearAllStatusBehavior extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
