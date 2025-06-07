package it.unibo.skalamon.model.behavior.modifier
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.data.Percentage

/** Modifiers that can be applied to a [[Behavior]].
  *
  * @param target
  *   The target the behavior should apply to. If not specified, the behavior
  *   will use the default target from its context.
  * @param probability
  *   The probability of the behavior being applied.
  */
case class BehaviorModifiers(
    target: Option[TargetModifier.Type] = None,
    probability: Option[Percentage] = None
)
