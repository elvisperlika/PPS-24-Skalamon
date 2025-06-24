package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.{Battle, hookBattleStateUpdate}
import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.behavior.{
  Behavior,
  BehaviorsContext,
  WithBehaviors
}
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** Represents the context of an ability that can be triggered in a battle.
  *
  * @param origin
  *   The ability this context is associated with.
  * @param target
  *   The target Pokémon of the ability.
  * @param source
  *   The source Pokémon that is triggering the ability.
  * @param behaviors
  *   Ordered behaviors that will be applied during the execution of the
  *   ability, associated with their modifiers.
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
  /** Creates an [[AbilityContext]] for the given ability, target, and source
    * Pokémon.
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

  /** Hooks all hooks of the ability to an event manager, allowing it to execute
    * behaviors when specific events occur in the battle.
    *
    * @param battle
    *   The battle in which the ability is being used. Its event manager will be
    *   used to register the hooks.
    * @param target
    *   The target Pokémon of the ability.
    * @param source
    *   The source Pokémon that owns the ability.
    */
  def hookAll(battle: Battle)(
      target: BattlePokemon,
      source: BattlePokemon
  ): Unit =
    ability.hooks.foreach { case (eventType, behavior) =>
      battle.hookBattleStateUpdate(eventType): (battleState, _) =>
        val context = createContext(
          _.hooks(eventType),
          target,
          source
        )
        context(battleState)
    }
