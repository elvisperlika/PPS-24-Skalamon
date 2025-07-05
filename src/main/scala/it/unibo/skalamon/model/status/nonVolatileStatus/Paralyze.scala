package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.data.{Percentage, RandomGenerator, percent}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}
import it.unibo.skalamon.model.status.NonVolatileStatus

/** Reduces Speed to 50% and has a 25% chance to skip the current turn.
  * @param generator
  *   The random number generator to use for determining the chance of skipping
  *   the turn.
  */
case class Paralyze(generator: RandomGenerator = RandomGenerator())
    extends NonVolatileStatus:
  override def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon =
    val updatedStats = pokemon.base.stats.base.updatedWith(Stat.Speed) {
      case Some(value) => Some(value / Paralyze.AttackReduction)
      case other       => other
    }
    pokemon.copy(
      base = pokemon.base.copy(stats =
        pokemon.base.stats.copy(base = updatedStats)
      ),
      skipsCurrentTurn =
        if Paralyze.TriggerChance.randomBoolean(using generator) then true
        else pokemon.skipsCurrentTurn
    )

object Paralyze:
  val AttackReduction: Int = 2
  val TriggerChance: Percentage = 25.percent
