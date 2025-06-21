package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.{Behavior, WithBehaviors}
import it.unibo.skalamon.model.data.RandomGenerator

/** A mixin trait for [[Behavior]] that applies a random modifier to the
  * behavior based on a range of values.
  *
  * @param min
  *   The minimum value for the random modifier.
  * @param max
  *   The maximum value for the random modifier.
  * @param behavior
  *   The behavior to apply with the random value.
  */
case class RandomModifier(min: Int, max: Int)(
    behavior: (value: Int) => Behavior
)(using generator: RandomGenerator = RandomGenerator())
    extends Behavior:

  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T =
    given BehaviorModifiers = modifiers
    val value = generator.nextInt(min, max)
    behavior(value)(container)
