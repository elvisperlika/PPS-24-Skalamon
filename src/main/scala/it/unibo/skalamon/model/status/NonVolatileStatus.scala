package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}

trait NonVolatileStatus extends Status:
  private val minPercent: Int = 0
  private val maxPercent: Int = 100

  /** Attempts to skip the current turn based on a given probability.
    * @param pokemon
    *   The Pokémon whose turn may be skipped.
    * @param probability
    *   The chance (in percent) to skip the turn.
    * @return
    *   A copy of the Pokémon with the `skipsCurrentTurn` flag set if the turn
    *   is skipped.
    */
  def skipTurn(pokemon: BattlePokemon, probability: Int): BattlePokemon =
    val randomValue = RandomGenerator().nextInt(minPercent, maxPercent)
    if randomValue < probability then
      pokemon.copy(skipsCurrentTurn = true)
    else
      pokemon

  /** Executes the effect of the non-volatile status on the given Pokémon.
    * @param pokemon
    *   The Pokémon to apply the status effect to.
    * @return
    *   A copy of the Pokémon with the status effect applied.
    */
  def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon

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
