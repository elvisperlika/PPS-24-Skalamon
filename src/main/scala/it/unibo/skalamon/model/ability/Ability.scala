package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the ability.
  */
case class Ability(
    name: String,
    onSwitchIn: Behavior = EmptyBehavior,
    onSwitchOut: Behavior = EmptyBehavior,
)
