package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.NonVolatileStatus

/** Inflicts damage equal to 1/16 of the maximum HP and halves the physical
  * Attack stat.
  */
case class Burn() extends NonVolatileStatus:
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    val updatedStats = pokemon.base.baseStats.base.updatedWith(Stat.Attack) {
      case Some(value) => Some(value / Burn.AttackReduction)
      case other       => other
    }
    pokemon.copy(
      currentHP = pokemon.currentHP - (pokemon.base.hp / Burn.DamageReduction),
      base =
        pokemon.base.copy(baseStats =
          pokemon.base.baseStats.copy(base = updatedStats)
        )
    )

object Burn:
  val AttackReduction = 2
  val DamageReduction = 16
