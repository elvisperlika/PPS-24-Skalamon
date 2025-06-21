package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.{Behavior, WithBehaviors}

/** A collection of [[Behavior]]s that, when applied, applies each sub-behavior.
  * Modifiers of this group are applied to each sub-behavior.
  */
case class BehaviorGroup(behaviors: Behavior*) extends Behavior:
  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T =
    given BehaviorModifiers = modifiers
    behaviors.foldLeft(container) { (current, behavior) => behavior(current) }
