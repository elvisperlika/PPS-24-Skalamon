package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}

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


case object Freeze extends NonVolatileStatus
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    )

case object Poison extends NonVolatileStatus

case object BadlyPoison extends NonVolatileStatus
