package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.move.MoveContext

/**
 * A behavior describes a single-responsibility trait or strategy
 * that can be applied to a move or ability in a battle.
 */
trait Behavior:
  protected def delegates: List[Behavior] = List(this)
  
  /**
   * Applies the behavior to the given move context.
   *
   * @param move The move context to which the behavior is applied.
   * @return A new move context with the behavior applied.
   */
  def apply(move: MoveContext)(using modifiers: BehaviorModifiers = BehaviorModifiers()): MoveContext =
    move.copy(behaviors = move.behaviors ++ delegates.map((_, modifiers)))

/**
 * An empty behavior that does nothing.
 */
object EmptyBehavior extends Behavior:
  override def apply(move: MoveContext)(using modifiers: BehaviorModifiers): MoveContext = move