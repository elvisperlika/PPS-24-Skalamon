package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.data.{Percentage, RandomGenerator, percent}
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.NonVolatileStatus

/** A status effect where the Pokémon cannot act while frozen. Each turn, there
  * is a 20% chance for the Pokémon to thaw out naturally.
  */
case class Freeze(generator: RandomGenerator = RandomGenerator())
    extends NonVolatileStatus:

  override def executeEffect(pokemon: BattlePokemon): BattlePokemon =
    if Freeze.ThawChance.randomBoolean(using generator) then
      pokemon.copy(
        nonVolatileStatus = None,
        skipsCurrentTurn = false
      )
    else
      pokemon.copy(skipsCurrentTurn = true)

object Freeze:
  val ThawChance: Percentage = 20.percent
