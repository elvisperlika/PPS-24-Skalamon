package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.move.MoveModel.{Accuracy, Category}
import it.unibo.skalamon.model.types.Type

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the move.
  * @param priority
  *   The priority of the move, which determines the order of execution relative
  *   to other moves.
  * @param moveType
  *   The type of the move (e.g., Fire, Water, etc.).
  * @param category
  *   The category of the move (e.g., Physical, Special, Status).
  * @param accuracy
  *   The accuracy of the move, which determines the likelihood of hitting the
  *   target.
  * @param pp
  *   The Power Points of the move, which indicate how many times the move can
  *   be used before it needs to be restored.
  * @param success
  *   The behavior of this move when it is successful.
  * @param fail
  *   The behavior of this move when it fails/misses.
  */
case class Move(
    name: String,
    priority: Int = 0,
    moveType: Type,
    category: Category,
    accuracy: Accuracy,
    pp: Int,
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
