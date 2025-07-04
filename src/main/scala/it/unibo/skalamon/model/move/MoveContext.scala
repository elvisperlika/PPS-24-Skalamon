package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.behavior.kind.SingleHitBehavior
import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.field.{FieldEffectMixin, PokemonRule}
import it.unibo.skalamon.model.behavior.visitor.BehaviorVisitor
import it.unibo.skalamon.model.behavior.{
  Behavior,
  BehaviorsContext,
  WithBehaviors
}
import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  FieldEffect,
  PokemonRules,
  Room,
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
  * @param behaviors
  *   Ordered behaviors that will be applied during the execution of the move,
  *   associated with their modifiers.
  */
case class MoveContext(
    override val origin: BattleMove,
    override val target: BattlePokemon,
    override val source: BattlePokemon,
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
    * @return
    *   A new [[MoveContext]] with the phase's behaviors applied.
    */
  def createContext(
      behavior: Move => Behavior,
      target: BattlePokemon,
      source: BattlePokemon
  ): MoveContext =
    behavior(move.move)(MoveContext(move, target, source))

  /** @param battle
    */
  def hookAllMove(battle: Battle): Unit =
//    move.move.success match
//      case wb: WeatherBehavior => wb.weather match
//          case w: Weather with PokemonRules =>
//            hookWeatherRules(w)
//          case _ => ()
//      case tb: TerrainBehavior => tb.terrain match
//          case t: Terrain with PokemonRules =>
//            hookTerrainRules(t)
//          case _ => ()
//      case rb: RoomBehavior => rb.room match
//          case r: Room with PokemonRules =>
//            hookRoomRules(r)
//          case _ => ()

    def hookTerrainRules(terrain: Terrain with PokemonRules): Unit =
      terrain.rules.foreach: (event, rule) =>
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.terrain match
            case Some(tr) if tr.getClass == terrain.getClass =>
              updateStateWithRules(rule: PokemonRule)
            case _ => state
        }

    def hookWeatherRules(terrain: Weather with PokemonRules)
        : Unit =
      terrain.rules.foreach: (event, rule) =>
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.weather match
            case Some(tr) if tr.getClass == terrain.getClass =>
              updateStateWithRules(rule: PokemonRule)
            case _ => state
        }

    def hookRoomRules(terrain: Room with PokemonRules)
        : Unit =
      terrain.rules.foreach: (event, rule) =>
        battle.hookBattleStateUpdate(event) { (state, _) =>
          state.field.room match
            case Some(tr) if tr.getClass == terrain.getClass =>
              updateStateWithRules(rule: PokemonRule)
            case _ => state
        }

    def updateStateWithRules(rule: PokemonRule): BattleState =
      val trainers = battle.currentTurn.get.state.snapshot.trainers
      val updTrainers =
        trainers.map(t => t.copy(team = t.team.map(rule(_))))
      battle.currentTurn.get.state.snapshot.copy(trainers =
        updTrainers
      )
