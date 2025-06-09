package it.unibo.skalamon.model.behavior.modifier
import it.unibo.skalamon.model.behavior.Behavior

/** Modifiers that can be applied to a [[Behavior]]. In most cases, behavior
  * modifiers (e.g. [[ProbabilityModifier]]) act as decorators and do not need
  * to affect [[BehaviorModifiers]]. Some behaviors, however, may need to affect
  * settings that cannot be modelled by a decorator, such as [[TargetModifier]].
  *
  * @param target
  *   The target the behavior should apply to. If not specified, the behavior
  *   will use the default target from its context.
  */
case class BehaviorModifiers(
    target: Option[TargetModifier.Type] = None
)
