package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.NonVolatileStatus

/** Inflicts damage equal to 1/16 of the maximum HP every turn, increasing by
  * 1/16 for each turn the Pokémon is poisoned. The damage starts at 1/16 and
  * increases by 1/16 each turn.
  * @param turnsPoisoned
  *   The number of turns the Pokémon has been poisoned.
  */
case class BadlyPoison(turnsPoisoned: Int = 1) extends NonVolatileStatus:
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    pokemon.copy(
      currentHP =
        pokemon.currentHP - ((pokemon.base.hp / BadlyPoison.DamageReduction) * turnsPoisoned),
      nonVolatileStatus =
        pokemon.nonVolatileStatus.map(_.copy(status =
          BadlyPoison(turnsPoisoned + 1)
        ))
    )

object BadlyPoison:
  val DamageReduction: Int = 16
