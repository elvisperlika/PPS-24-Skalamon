package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.modifier.TargetModifier
import it.unibo.skalamon.model.pokemon.MutablePokemon

/** Represents the context of a move that can be executed in a battle.
  *
  * @param move
  *   The move this context is associated with.
  * @param target
  *   The target Pokémon of the move.
  * @param source
  *   The source Pokémon that is executing the move.
  * @param hits
  *   Ordered hits that will occur during the execution of the move, each with
  *   its own power.
  */
case class MoveContext(
    move: MutableMove,
    target: MutablePokemon,
    source: MutablePokemon,
    var hits: List[MoveContext.Hit] = List.empty
)

object MoveContext:
  /** A single hit caused by a move by [[MoveContext]]'s source on
    * [[MoveContext]]'s target.
    *
    * @param power
    *   The base power of the hit.
    */
  case class Hit(power: Int, target: Option[TargetModifier.Type])
