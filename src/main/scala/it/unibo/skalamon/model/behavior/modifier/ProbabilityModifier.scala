package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.data.Percentage

/** A mixin trait for [[Behavior]] that allows specifying a probability of
  * success for a behavior.
  * @param percentage
  *   The probability of applying the behavior.
  */
trait ProbabilityModifier(percentage: Percentage) extends ModifierBehavior:
  override def apply(modifiers: BehaviorModifiers): BehaviorModifiers =
    modifiers.copy(probability = Some(percentage))
