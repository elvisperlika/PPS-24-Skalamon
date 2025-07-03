package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  hookBattleStateUpdate
}
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
  * @param turnIndex
  *   The index of the turn in which the ability is being executed.
  */
case class AbilityContext(
    override val origin: Ability,
    override val target: BattlePokemon,
    override val source: BattlePokemon,
    override val turnIndex: Int = 0,
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
    * @param turnIndex
    *   The index of the turn in which the ability is being executed.
    * @return
    *   A new [[AbilityContext]] with the phase's behaviors applied.
    */
  def createContext(
      behavior: Ability => Behavior,
      target: BattlePokemon,
      source: BattlePokemon,
      turnIndex: Int = 0
  ): AbilityContext =
    behavior(ability)(AbilityContext(ability, target, source))

  /** Hooks all hooks of the ability to an event manager, allowing it to execute
    * behaviors when specific events occur in the battle. An update to the
    * battle state will be triggered only if both the target and source Pokémon
    * are defined.
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
    target: BattleState => Option[BattlePokemon],
    source: BattleState => Option[BattlePokemon]
  ): Unit =
    ability.hooks.foreach: hook =>
      battle.hookBattleStateUpdate(hook.eventType): (battleState, data) =>
        (target(battleState), source(battleState)) match
          case (Some(t), Some(s)) =>
            val context = createContext(
              _ => hook.behavior(s, t, data),
              t,
              s,
              battle.turnIndex
            )
            context(battleState)

          case _ => battleState
