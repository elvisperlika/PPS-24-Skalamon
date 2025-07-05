package it.unibo.skalamon.model.status.volatileStatus

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.VolatileStatus

/** Blocks moves that target this Pokémon. */
case class ProtectEndure() extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 1):
  override def executeEffect(
      pokemon: BattlePokemon,
      assignedTurn: Int,
      currentTurn: Int
  ): BattlePokemon = pokemon.copy(isProtected = true)
