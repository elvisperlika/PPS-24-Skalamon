package it.unibo.skalamon.model.behavior.modifier

/** A mixin trait for [[Behavior]] that allows specifying a target that is
  * different from the context's.
  * @param target
  *   The new target type.
  */
trait TargetModifier(target: TargetModifier.Type) extends ModifierBehavior:
  override protected def apply(
      modifiers: BehaviorModifiers
  ): BehaviorModifiers = modifiers.copy(target = Some(target))

object TargetModifier:
  /** Possible targets for a behavior. */
  enum Type:
    /** The target is the context's source. */
    case Self

    /** The target is the context's target. */
    case Other
