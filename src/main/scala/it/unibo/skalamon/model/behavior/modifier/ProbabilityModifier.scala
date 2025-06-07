package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.data.{Percentage, RandomGenerator}
import it.unibo.skalamon.model.move.MoveContext

/** A mixin trait for [[Behavior]] that allows specifying a probability of
  * success for a behavior.
  * @param percentage
  *   The probability of applying the behavior.
  */
trait ProbabilityModifier(percentage: Percentage)(using
    generator: RandomGenerator = RandomGenerator()
) extends Behavior:
  abstract override def apply(context: MoveContext)(using
      modifiers: BehaviorModifiers
  ): MoveContext =
    given BehaviorModifiers = modifiers
    given RandomGenerator = generator
    if (percentage.randomBoolean) {
      super.apply(context)
    } else {
      EmptyBehavior.apply(context)
    }
