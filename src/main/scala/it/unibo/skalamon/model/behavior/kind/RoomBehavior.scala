package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.field.FieldEffectMixin.Room

/** Behavior that applies a room to the field.
  * @param room
  *   The room to be applied, with the current turn index as a parameter
  */
case class RoomBehavior(room: (turnIndex: Int) => Room) extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
