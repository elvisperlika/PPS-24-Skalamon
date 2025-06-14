package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.EventType

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the ability.
  * @param hooks
  *   Behaviors that are triggered at specific events in a battle.
  */
case class Ability(
    name: String,
    hooks: Map[EventType[_], Behavior]
)
