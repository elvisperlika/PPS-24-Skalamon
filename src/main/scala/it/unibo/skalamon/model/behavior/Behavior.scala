package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.move.MoveContext

/**
 * A behavior describes a single-responsibility trait or strategy
 * that can be applied to a move or ability in a battle.
 */
trait Behavior {
  /**
   * Applies the behavior to the given move context.
   *
   * @param move The move context to which the behavior is applied.
   * @return A new move context with the behavior applied.
   */
  def apply(move: MoveContext): MoveContext
}
