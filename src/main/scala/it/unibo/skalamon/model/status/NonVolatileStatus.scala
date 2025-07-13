package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.BattlePokemon

/** Represents the Non-Volatile status of a Pokémon.
  */
trait NonVolatileStatus extends Status:
  /** Executes the effect of the non-volatile status on the given Pokémon.
    * @param pokemon
    *   The Pokémon to apply the status effect to.
    * @return
    *   A copy of the Pokémon with the status effect applied.
    */
  def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon
