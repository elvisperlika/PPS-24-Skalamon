package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.Action

/** Utility object containing implicit ordering definitions used to sort battle
  * actions.
  */
object ClassicOrdering:

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
  import OrderingUtils.*
  given Ordering[Action] =
    actionOrdering((s1, s2) => s2.compareTo(s1))
