package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  Trainer,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.behavior.kind.{
  RoomBehavior,
  SideConditionBehavior,
  SingleHitBehavior,
  TerrainBehavior,
  WeatherBehavior
}
import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.field.{FieldEffectMixin, PokemonRule}
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.behavior.{
  Behavior,
  BehaviorsContext,
  WithBehaviors
}
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  FieldEffect,
  PokemonRules,
  Room,
  SideCondition,
  Terrain,
  Weather
}
import it.unibo.skalamon.model.field.weather.{Rain, Sandstorm, Snow, Sunny}
import it.unibo.skalamon.model.pokemon.*

/** Represents the context of a move that can be executed in a battle.
  *
  * @param origin
  *   The move this context is associated with.
  * @param target
  *   The target Pokémon of the move.
  * @param source
  *   The source Pokémon that is executing the move.
  * @param turnIndex
  *   The index of the turn in which the move is being executed.
  * @param behaviors
  *   Ordered behaviors that will be applied during the execution of the move,
  *   associated with their modifiers.
  */
case class MoveContext(
    override val origin: BattleMove,
    override val target: BattlePokemon,
    override val source: BattlePokemon,
    override val turnIndex: Int = 0,
    override val behaviors: List[(Behavior, BehaviorModifiers)] = List.empty
) extends BehaviorsContext[BattleMove]:

  override def append[T <: WithBehaviors](
      newBehaviors: List[(Behavior, BehaviorModifiers)]
  ): T =
    this.copy(behaviors = behaviors ++ newBehaviors).asInstanceOf[T]

extension (move: BattleMove)
  /** Creates a [[MoveContext]] for the given move, target, and source Pokémon.
    *
    * @param behavior
    *   Supplier of the behavior of the move execution (success or fail).
    * @param target
    *   The target Pokémon of the move.
    * @param source
    *   The source Pokémon that is executing the move.
    * @param turnIndex
    *   The index of the turn in which the move is being executed.
    * @return
    *   A new [[MoveContext]] with the phase's behaviors applied.
    */
  def createContext(
      behavior: Move => Behavior,
      target: BattlePokemon,
      source: BattlePokemon,
      turnIndex: Int = 0
  ): MoveContext =
    behavior(move.move)(MoveContext(move, target, source, turnIndex))
