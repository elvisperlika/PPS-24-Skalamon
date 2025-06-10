package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.Behavior

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
    success: MovePhase = MovePhase(List.empty),
    fail: MovePhase = MovePhase(List.empty)
)

/** Represents an execution case of a move under certain circumstances. Multiple
  * cases compose a move.
  *
  * @param behaviors
  *   The list of behaviors to be executed in this phase.
  */
case class MovePhase(behaviors: List[Behavior])

/** A move in the context of a battle.
  *
  * @param move
  *   The base move.
  * @param pp
  *   The current Power Points of the move.
  */
case class BattleMove(move: Move, pp: Int)
