package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.behavior.{
  Behavior,
  BehaviorsContext,
  WithBehaviors
}
import it.unibo.skalamon.model.pokemon.*

/** Represents the context of a move that can be executed in a battle.
  *
  * @param origin
  *   The move this context is associated with.
  * @param target
  *   The target Pokémon of the move.
  * @param source
  *   The source Pokémon that is executing the move.
  * @param behaviors
  *   Ordered behaviors that will be applied during the execution of the move,
  *   associated with their modifiers.
  */
case class AbilityContext(
    override val origin: Ability,
    override val target: BattlePokemon,
    override val source: BattlePokemon,
    override val behaviors: List[(Behavior, BehaviorModifiers)] = List.empty
) extends BehaviorsContext[Ability]:

  override def append[T <: WithBehaviors](
      newBehaviors: List[(Behavior, BehaviorModifiers)]
  ): T =
    this.copy(behaviors = behaviors ++ newBehaviors).asInstanceOf[T]

extension (ability: Ability)
  /** Creates an [[AbilityContext]] for the given ability, target, and source Pokémon.
    *
    * @param behavior
    *   Supplier of the behavior of the ability execution.
    * @param target
    *   The target Pokémon of the ability.
    * @param source
    *   The source Pokémon that owns the ability.
    * @return
    *   A new [[AbilityContext]] with the phase's behaviors applied.
    */
  def createContext(
      behavior: Ability => Behavior,
      target: BattlePokemon,
      source: BattlePokemon
  ): AbilityContext =
    behavior(ability)(AbilityContext(ability, target, source))
