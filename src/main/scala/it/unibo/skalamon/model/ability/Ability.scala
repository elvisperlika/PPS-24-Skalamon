package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.turn.BattleEvents
import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.{
  ProbabilityModifier,
  TargetModifier
}
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.event.{
  ActionEvents,
  BehaviorEvent,
  EventType,
  TurnStageEvents
}
import it.unibo.skalamon.model.field.weather.{Rain, Sandstorm}
import it.unibo.skalamon.model.move.MoveContext
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.Paralyze

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
  import it.unibo.skalamon.model.dsl.*

  /** An ability with no effect. */
  def none: Ability =
    Ability("No Ability", List.empty)

  /** When the Pokémon switches in, lowers the opponent's attack. */
  def intimidate: Ability =
    ability("Intimidate"):
      _.on(ActionEvents.Switch): (source, _, switch) =>
        if switch.in is source then
          StatChangeBehavior(Stat.Attack - 1)
        else
          nothing

  /** At the beginning of each turn, the Pokémon's speed is increased. */
  def speedBoost: Ability =
    import it.unibo.skalamon.model.behavior.kind.+
    ability("Speed Boost"):
      _.on(TurnStageEvents.Started): (_, _, _) =>
        new StatChangeBehavior(Stat.Speed + 1)
          with TargetModifier(TargetModifier.Type.Self)

  /** When rain is created, raises the Pokémon's attack. */
  def swiftSwim: Ability =
    import it.unibo.skalamon.model.behavior.kind.+
    ability("Swift Swim"):
      _.on(BehaviorEvent[WeatherBehavior]()): (_, _, behavior) =>
        behavior match
          case (b: WeatherBehavior, _) if b.weather(0).isInstanceOf[Rain] =>
            new StatChangeBehavior(Stat.Attack + 1)
              with TargetModifier(TargetModifier.Type.Self)
          case _ =>
            nothing

  /** When the Pokémon is assigned a status, copies it to the opponent.
    */
  def synchronize: Ability =
    ability("Synchronize"):
      _.on(BehaviorEvent[StatusBehavior]()): (source, target, behavior) =>
        behavior match
          case (b, context) if context.target is source => b
          case _                                        => nothing

  /** Prevents any stat drop. */
  def clearBody: Ability =
    ability("Clear Body"):
      _.on(BehaviorEvent[StatChangeBehavior]()): (source, _, behavior) =>
        behavior match
          case (b, context) if b.change.stage < 0 && (context.target is source) =>
            new StatChangeBehavior(b.change.copy(stage = -b.change.stage))
              with TargetModifier(TargetModifier.Type.Self)
          case _ => nothing

  /** Reverses any stat change. */
  def contrary: Ability =
    ability("Contrary"):
      _.on(BehaviorEvent[StatChangeBehavior]()): (source, _, behavior) =>
        behavior match
          case (b, context) if context.target is source =>
            new StatChangeBehavior(b.change.copy(stage = -b.change.stage * 2))
              with TargetModifier(TargetModifier.Type.Self)
          case _ => nothing

  /** When the Pokémon switches in, sets the weather to rain. */
  def drizzle: Ability =
    ability("Drizzle"):
      _.on(ActionEvents.Switch): (source, _, switch) =>
        if switch.in is source then
          WeatherBehavior(Rain(_))
        else
          nothing

  /** When the Pokémon switches in, sets the weather to sandstorm. */
  def sandStream: Ability =
    ability("Sand Stream"):
      _.on(ActionEvents.Switch): (source, _, switch) =>
        if switch.in is source then
          WeatherBehavior(Sandstorm(_))
        else
          nothing


  /** If hit by a physical move, the opponent has a chance to be paralyzed */
  def static: Ability =
    ability("Static"):
      _.on(BehaviorEvent[SingleHitBehavior]()): (source, target, behavior) =>
        behavior match
          case (b, context: MoveContext)
              if (context.target is source) && (context.origin.move.category == Physical) =>
            new StatusBehavior(_ => Paralyze)
              with ProbabilityModifier(30.percent)
          case _ => nothing

  /** When the Pokémon switches out, clears all its statuses */
  def naturalCure: Ability =
    ability("Natural Cure"):
      // TODO add `out` to ActionEvents.Switch
      _.on(BattleEvents.PokemonSwitchOut): (source, _, switched) =>
        if switched is source then
          new ClearAllStatusBehavior
            with TargetModifier(TargetModifier.Type.Self)
        else
          nothing
