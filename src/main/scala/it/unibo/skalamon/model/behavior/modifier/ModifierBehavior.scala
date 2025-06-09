package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.{Behavior, WithBehaviors}

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

  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T =
    given BehaviorModifiers = this (modifiers)
    super.apply(container)
