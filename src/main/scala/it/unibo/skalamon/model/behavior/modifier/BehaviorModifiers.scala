package it.unibo.skalamon.model.behavior.modifier
import it.unibo.skalamon.model.behavior.Behavior

/** Modifiers that can be applied to a [[Behavior]].
 *
  * @param target
  *   The target the behavior should apply to. If not specified, the behavior
  *   will use the default target from its context.
  */
case class BehaviorModifiers(
    target: Option[TargetModifier.Type] = None
)
