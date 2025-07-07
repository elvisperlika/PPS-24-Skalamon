package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.field.FieldEffectMixin.Terrain

/** Behavior that applies a terrain to the field.
  * @param terrain
  *   The terrain to be applied, with the current turn index as a parameter
  */
case class TerrainBehavior(terrain: (turnIndex: Int) => Terrain)
    extends Behavior:
  override def accept[T](visitor: BehaviorVisitor[T]): T = visitor.visit(this)
