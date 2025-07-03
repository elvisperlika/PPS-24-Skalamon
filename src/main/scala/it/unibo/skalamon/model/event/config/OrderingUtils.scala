package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{Action, MoveAction}
import it.unibo.skalamon.model.pokemon.Stat.Speed

/** Utility object containing implicit ordering definitions used to sort battle
  * actions.
  */
object OrderingUtils:

  /** Provides an implicit [[Ordering]] instance for [[Action]].
    *
    * The comparison rules are as follows:
    *   - Actions are first ordered by their `priority` value in **descending**
    *     order (higher priority acts first).
    *   - If priorities are equal and both actions are [[MoveAction]], the
    *     action with higher Pokémon speed goes first.
    *   - If both actions have same priority and are not both [[MoveAction]],
    *     order is undefined (returns 0).
    *
    * @return
    *   an [[Ordering]] instance for sorting [[Action]] elements
    */
  given Ordering[Action] with
    def compare(a1: Action, a2: Action): Int =
      def speedOf(a: Action): Int = a match
        case MoveAction(battleMove, source, target) =>
          source.inField.get.base.stats.base(Speed)
        case _ => 0

      val p1 = a1.priority
      val p2 = a2.priority
      if p1 != p2 then p2.compareTo(p1)
      else
        (a1, a2) match
          case (m1: MoveAction, m2: MoveAction) =>
            speedOf(m2).compareTo(speedOf(m1))
          case _ => 0
