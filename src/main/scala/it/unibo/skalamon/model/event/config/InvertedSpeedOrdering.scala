package it.unibo.skalamon.model.event.config

import it.unibo.skalamon.controller.battle.action.{Action, MoveAction}
import it.unibo.skalamon.model.pokemon.Stat.Speed

/** Alternative ordering: slower Pokémon act first at equal priority */
object InvertedSpeedOrdering:

  /** Provides an Ordering[Action] where:
    *   - Higher priority acts first
    *   - If priorities are equal and both are MoveActions, the slower Pokémon
    *     acts first
    */
  import OrderingUtils.*
  given Ordering[Action] =
    actionOrdering((s1, s2) => s1.compareTo(s2)) // velocità crescente
