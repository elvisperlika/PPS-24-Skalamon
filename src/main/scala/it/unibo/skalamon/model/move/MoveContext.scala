package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.behavior.{Behavior, WithBehaviors}
import it.unibo.skalamon.model.pokemon.MutablePokemon

/** Represents the context of a move that can be executed in a battle.
  *
  * @param move
  *   The move this context is associated with.
  * @param target
  *   The target Pokémon of the move.
  * @param source
  *   The source Pokémon that is executing the move.
  * @param behaviors
  *   Ordered behaviors that will be applied during the execution of the move,
  *   associated with their modifiers.
  */
case class MoveContext(
    move: MutableMove,
    target: MutablePokemon,
    source: MutablePokemon,
    override val behaviors: List[(Behavior, BehaviorModifiers)] = List.empty
) extends WithBehaviors:

  override def append[T <: WithBehaviors](
      newBehaviors: List[(Behavior, BehaviorModifiers)]
  ): T =
    this.copy(behaviors = behaviors ++ newBehaviors).asInstanceOf[T]
