package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.turn.BattleEvents
import it.unibo.skalamon.model.behavior.kind.{
  +,
  -,
  StatChangeBehavior,
  StatusBehavior
}
import it.unibo.skalamon.model.behavior.modifier.TargetModifier
import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.event.{BehaviorEvent, EventType}
import it.unibo.skalamon.model.field.weather.Rain
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the ability.
  * @param hooks
  *   Behaviors that are triggered at specific events in a battle.
  */
case class Ability(
    name: String,
    hooks: List[AbilityHook[_]]
)

/** A hook that defines a behavior to be executed when a specific event occurs
  * in the battle.
  *
  * @param eventType
  *   The type of event that triggers the behavior.
  * @param behavior
  *   The behavior to execute when the event occurs.
  */
case class AbilityHook[T](
    eventType: EventType[T],
    behavior: (
        source: BattlePokemon,
        target: BattlePokemon,
        data: T
    ) => Behavior
)

/** Factory for abilities.
  */
object Ability:
  /** When the Pokémon switches in, lowers the opponent's attack. */
  def intimidate: Ability =
    ability("Intimidate"):
      _.on(BattleEvents.PokemonSwitchIn): (source, target, switched) =>
        if switched is source then
          StatChangeBehavior(Stat.Attack - 1)
        else
          EmptyBehavior

  /** When rain is created, raises the Pokémon's attack. */
  def swiftSwim: Ability =
    ability("Swift Swim"):
      _.on(BattleEvents.CreateWeather): (_, _, weather) =>
        if weather.isInstanceOf[Rain] then
          new StatChangeBehavior(Stat.Speed + 1)
            with TargetModifier(TargetModifier.Type.Self)
        else
          EmptyBehavior

  /** When the Pokémon is assigned a status, copies it to the opponent.
    */
  def synchronize: Ability =
    ability("Synchronize"):
      _.on(BehaviorEvent[StatusBehavior]()): (source, target, behavior) =>
        behavior match
          case (b, context) if context.target is source => b
          case _                                        => EmptyBehavior
