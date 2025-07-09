package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.NonVolatileStatus

/** Inflicts damage equal to 1/16 of the maximum HP */
case class Poison() extends NonVolatileStatus:
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    pokemon.copy(currentHP =
      pokemon.currentHP - (pokemon.base.hp / Poison.DamageReduction)
    )

object Poison:
  val DamageReduction: Int = 16
