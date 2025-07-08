package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.BattlePokemon

trait VolatileStatus extends Status:
  def executeEffect(
      pokemon: BattlePokemon,
      currentTurn: Int,
      assignedTurn: Int
  ): BattlePokemon
