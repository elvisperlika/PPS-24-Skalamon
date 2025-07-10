package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.BattlePokemon

/** Represents the Volatile status of a Pokémon.
  */
trait VolatileStatus extends Status:
  /** Executes the effect of the volatile status on the given Pokémon.
    * @param pokemon
    *   The Pokémon to apply the status effect to.
    * @param currentTurn
    *   The current turn in the battle.
    * @param assignedTurn
    *   The turn when this status was assigned.
    * @return
    *   A copy of the Pokémon with the status effect applied.
    */
  def executeEffect(
      pokemon: BattlePokemon,
      currentTurn: Int,
      assignedTurn: Int
  ): BattlePokemon
