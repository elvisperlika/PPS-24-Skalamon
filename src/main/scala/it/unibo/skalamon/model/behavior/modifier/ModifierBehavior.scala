package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.move.MoveContext

/** A mixin trait for [[Behavior]] that allows modifying the behavior itself by
  * means of new [[BehaviorModifiers]].
  */
trait ModifierBehavior extends Behavior:
  /** Applies the behavior modifiers to the current behavior.
    *
    * @param modifiers
    *   The current behavior modifiers.
    * @return
    *   A new set of behavior modifiers that includes the modifications.
    */
  protected def apply(modifiers: BehaviorModifiers): BehaviorModifiers

  abstract override def apply(context: MoveContext)(using
      modifiers: BehaviorModifiers
  ): MoveContext =
    given BehaviorModifiers = this(modifiers)
    super.apply(context)
