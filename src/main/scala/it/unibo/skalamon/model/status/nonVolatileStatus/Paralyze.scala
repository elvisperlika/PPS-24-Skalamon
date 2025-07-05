package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.NonVolatileStatus

/** Reduces Speed to 50% and has a 25% chance to skip the current turn. */
case class Paralyze() extends NonVolatileStatus:
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    val updatedStats = pokemon.base.baseStats.base.updatedWith(Stat.Speed) {
      case Some(value) => Some(value / Paralyze.AttackReduction)
      case other       => other
    }
    val updatedPokemon = pokemon.copy(
      base = pokemon.base.copy(
        baseStats = pokemon.base.baseStats.copy(base = updatedStats)
      )
    )
    skipTurn(updatedPokemon, Paralyze.TriggerChance)

object Paralyze:
  val AttackReduction: Int = 2
  val TriggerChance: Int = 25