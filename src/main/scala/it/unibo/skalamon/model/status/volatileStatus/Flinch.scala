package it.unibo.skalamon.model.status.volatileStatus

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.VolatileStatus

/** Makes the Pokémon lose one turn */
case class Flinch() extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 0):
  override def executeEffect(
      pokemon: BattlePokemon,
      assignedTurn: Int,
      currentTurn: Int
  ): BattlePokemon = pokemon.copy(skipsCurrentTurn = true)
