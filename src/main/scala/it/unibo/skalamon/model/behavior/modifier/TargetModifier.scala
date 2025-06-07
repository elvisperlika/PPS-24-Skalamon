package it.unibo.skalamon.model.behavior.modifier

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.move.MoveContext

/** A mixin trait for [[Behavior]] that allows specifying a target that is
  * different from the context's.
  * @param target
  *   The new target type.
  */
trait TargetModifier(target: TargetModifier.Type) extends Behavior:
  abstract override def apply(context: MoveContext)(using
      modifiers: BehaviorModifiers
  ): MoveContext =
    given BehaviorModifiers = modifiers.copy(target = Some(target))
    super.apply(context)

object TargetModifier:
  /** Possible targets for a behavior. */
  enum Type:
    /** The target is the context's source. */
    case Self

    /** The target is the context's target. */
    case Other
