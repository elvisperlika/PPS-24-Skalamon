package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.move.MoveModel.Category
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.pokemon.Stat.{
  Attack,
  Defense,
  SpecialAttack,
  SpecialDefense
}
import it.unibo.skalamon.model.types.TypeUtility

object DamageCalculatorGen1:

  private val NoMultiplier: Int = 1
  private val LevelMultiplier: Int = 2
  private val LevelDivider: Int = 5
  private val LevelBoost: Int = 2
//  private val RandomFactorMinBound: Int = 85
//  private val RandomFactorMaxBound: Int = 100
//  private val PercentageDivider: Int = 100
  private val DamageDivider: Int = 50
  private val StabMultiplier: Int = 2

  def calculate(
      origin: BattleMove,
      source: BattlePokemon,
      target: BattlePokemon,
      power: Int
  ): Int =
    val sourceStat = source.base.stats.base
    val targetStat = target.base.stats.base

    val categoryScalar: Double = origin.move.category match
      case Category.Physical => sourceStat(Attack) / targetStat(Defense)
      case Category.Special  =>
        sourceStat(SpecialAttack) * targetStat(SpecialDefense)
      case _ => NoMultiplier

    val levelScalar = LevelMultiplier * source.level / LevelDivider + LevelBoost

    val baseDamage: Double =
      levelScalar * power * categoryScalar / DamageDivider

    val multiplier: Double =
      val stab: Double =
        if source.base.types.contains(origin.move.moveType)
        then StabMultiplier
        else NoMultiplier

      val typeEfficacy: Double = TypeUtility.calculateTypeMultiplier(
        origin.move.moveType.computeEffectiveness(target.base.types)
      )

      // Comment this factor for testing
      /* val randomFactor = RandomGenerator().nextInt(
        RandomFactorMinBound,
        RandomFactorMaxBound
      ) / PercentageDivider */

      stab * typeEfficacy // * randomFactor

    (baseDamage * multiplier).toInt
