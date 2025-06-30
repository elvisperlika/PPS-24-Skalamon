package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the move.
  * @param success
  *   The behavior of this move when it is successful.
  * @param fail
  *   The behavior of this move when it fails/misses.
  */
case class Move(
    name: String,
    priority: Int = 0,
    success: Behavior = EmptyBehavior,
    fail: Behavior = EmptyBehavior
)

/** A move in the context of a battle.
  *
  * @param move
  *   The base move.
  * @param pp
  *   The current Power Points of the move.
  */
case class BattleMove(move: Move, pp: Int)
