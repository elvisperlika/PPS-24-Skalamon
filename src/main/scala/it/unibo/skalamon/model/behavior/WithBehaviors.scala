package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers

/** A collection of [[Behavior]]s that can be applied to a context, such as a
  * move or ability in a battle.
  */
trait WithBehaviors:
  /** The list of behaviors associated with this context, each paired with its
    * corresponding [[BehaviorModifiers]].
    */
  val behaviors: List[(Behavior, BehaviorModifiers)]

  /** Appends new behaviors to the existing list of behaviors.
    *
    * @param newBehaviors
    *   A list of tuples where each tuple contains a behavior and its associated
    *   modifiers.
    * @tparam T
    *   The type of the container that holds the behaviors.
    * @return
    *   A new instance of the container with the appended behaviors.
    */
  def append[T <: WithBehaviors](
      newBehaviors: List[(Behavior, BehaviorModifiers)]
  ): T
