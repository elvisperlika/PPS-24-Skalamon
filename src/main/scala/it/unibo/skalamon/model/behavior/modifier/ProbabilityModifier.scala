package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior, WithBehaviors}
import it.unibo.skalamon.model.data.{Percentage, RandomGenerator}

/** A mixin trait for [[Behavior]] that allows specifying a probability of
  * success for a behavior.
  * @param percentage
  *   The probability of applying the behavior.
  */
trait ProbabilityModifier(percentage: Percentage)(using
    generator: RandomGenerator = RandomGenerator()
) extends Behavior:
  override def apply[T <: WithBehaviors](container: T)(using
      modifiers: BehaviorModifiers
  ): T =
    given BehaviorModifiers = modifiers
    given RandomGenerator = generator
    if (percentage.randomBoolean) {
      super.apply(container)
    } else {
      EmptyBehavior.apply(container)
    }
