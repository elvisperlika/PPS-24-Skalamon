package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.field
import it.unibo.skalamon.model.field.FieldEffectMixin.SideCondition

/** Behavior that applies a side condition to the field.
  * @param sideCondition
  *   The side condition to be applied, with the current turn index as a
  *   parameter
  */
case class SideConditionBehavior(sideCondition: (turnIndex: Int) => (
    Trainer,
    SideCondition
)) extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
