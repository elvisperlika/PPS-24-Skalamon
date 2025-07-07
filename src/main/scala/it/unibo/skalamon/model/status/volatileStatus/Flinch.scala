package it.unibo.skalamon.model.status.volatileStatus

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.VolatileStatus

/** Makes the Pokémon lose one turn */
case class Flinch(private val creationTurn: Int = 0) extends VolatileStatus,
      Expirable(creationTurn = creationTurn, duration = 0):
  override def executeEffect(
      pokemon: BattlePokemon,
      assignedTurn: Int,
      currentTurn: Int
  ): BattlePokemon = pokemon.copy(skipsCurrentTurn = true)
