package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorUtility.{
  calculateCategoryScalar,
  calculateFieldFactor,
  calculateStabFactor,
  calculateTypeEfficacy
}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

case object DamageCalculatorGen1 extends DamageCalculator:

  private val DamageDivider: Int = 50
  private val DamageBoost: Int = 2

  def calculate(
      origin: BattleMove,
      source: BattlePokemon,
      target: BattlePokemon,
      power: Int,
      battleState: BattleState
  ): Int =

    val baseDamage: Double =
      val categoryScalar: Double =
        calculateCategoryScalar(origin, source.actualStats, target.actualStats)
      power * categoryScalar / DamageDivider + DamageBoost

    val multiplier: Double =
      val stab: Double =
        calculateStabFactor(source.base.types, origin.move.moveType)
      val typeEfficacy: Double =
        calculateTypeEfficacy(origin.move.moveType, target.base.types)
      /* Comment this factor for testing */
      // val randomFactor = calculateRandomFactor
      val fieldFactor: Double = calculateFieldFactor(
        origin.move.moveType,
        source,
        target,
        battleState
      )
      stab * typeEfficacy * fieldFactor // * randomFactor

    (baseDamage * multiplier).toInt
