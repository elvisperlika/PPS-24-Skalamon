package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}

trait NonVolatileStatus extends Status:
  private val minPercent: Int = 0
  private val maxPercent: Int = 100

  def skipTurn(pokemon: BattlePokemon, probability: Int): BattlePokemon =
    val randomValue = RandomGenerator().nextInt(minPercent, maxPercent)
    if randomValue < probability then
      pokemon.copy(skipsCurrentTurn = true)
    else
      pokemon

  def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon

/** Inflicts damage equal to 1/16 of the maximum HP and halves the physical
  * Attack stat.
  */
case object Burn extends NonVolatileStatus:
  val BurnAttackReduction = 2
  val BurnDamageReduction = 16

  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    val updatedStats = pokemon.base.baseStats.base.updatedWith(Stat.Attack) {
      case Some(value) => Some(value / BurnAttackReduction)
      case other       => other
    }
    pokemon.copy(
      currentHP = pokemon.currentHP - (pokemon.base.hp / BurnDamageReduction),
      base =
        pokemon.base.copy(baseStats =
          pokemon.base.baseStats.copy(base = updatedStats)
        )
    )

/** Reduces Speed to 50% and has a 25% chance to skip the current turn. */
case object Paralyze extends NonVolatileStatus:
  val ParalyzeAttackReduction = 2
  val ParalyzeTriggerChance = 25

case object Freeze extends NonVolatileStatus
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    val updatedStats = pokemon.base.baseStats.base.updatedWith(Stat.Speed) {
      case Some(value) => Some(value / ParalyzeAttackReduction)
      case other       => other
    }
    val updatedPokemon = pokemon.copy(
      base = pokemon.base.copy(
        baseStats = pokemon.base.baseStats.copy(base = updatedStats)
      )
    )
    skipTurn(updatedPokemon, ParalyzeTriggerChance)

case object Poison extends NonVolatileStatus

case object BadlyPoison extends NonVolatileStatus
