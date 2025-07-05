package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.modifier.BehaviorGroup
import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}

/** DSL function to create a group of behaviors.
  *
  * @param behaviors
  *   The behaviors to group together.
  * @return
  *   A Behavior that represents the group of provided behaviors.
  */
def groupOf(behaviors: Behavior*): Behavior =
  BehaviorGroup(behaviors: _*)

/**
 * @return A behavior that does nothing.
 */
def nothing: Behavior = EmptyBehavior