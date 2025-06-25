package it.unibo.skalamon.model.behavior.visitor

import it.unibo.skalamon.model.behavior.BehaviorsContext
import it.unibo.skalamon.model.behavior.modifier.{
  BehaviorModifiers,
  TargetModifier
}
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** A [[BehaviorVisitor]] that operates within a [[BehaviorVisitor]] with
  * possible modifiers.
  */
trait ContextualBehaviorVisitor[T] extends BehaviorVisitor[T]:
  /** The context in which the behaviors are contained. */
  val context: BehaviorsContext[_]

  /** The modifiers applied to the behaviors in this context. */
  val modifiers: BehaviorModifiers

  /** @return the target Pokémon, based on the context target and modifiers. */
  protected def target: BattlePokemon =
    modifiers.target match
      case Some(TargetModifier.Type.Self) => context.source
      case _                              => context.target
