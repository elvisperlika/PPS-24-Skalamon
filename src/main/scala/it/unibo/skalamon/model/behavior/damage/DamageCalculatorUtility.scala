package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.{BattleState, Trainer}
import it.unibo.skalamon.model.behavior.damage.DamageCalculatorGen1.LevelMultiplier
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Room,
  SideCondition,
  Terrain,
  TypesModifier,
  Weather
}
import it.unibo.skalamon.model.field.fieldside.FieldSide
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.move.MoveModel.Category
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.pokemon.Stat.*
import it.unibo.skalamon.model.types.{Type, TypeUtility}

object DamageCalculatorUtility:

  private val NoMultiplier: Int = 1
  private val LevelMultiplier: Int = 2
  private val LevelDivider: Int = 5
  private val LevelBoost: Int = 2
  private val StabMultiplier: Int = 2
  private val RandomFactorMinBound: Int = 85
  private val RandomFactorMaxBound: Int = 100
  private val PercentageDivider: Int = 100

  def calculateCategoryScalar(
      battleMove: BattleMove,
      sourceStat: Stats,
      targetStat: Stats
  ): Double =
    battleMove.move.category match
      case Category.Physical =>
        sourceStat.base(Attack) / targetStat.base(Defense)
      case Category.Special =>
        sourceStat.base(SpecialAttack) * targetStat.base(SpecialDefense)
      case _ => NoMultiplier

  def calculateLevelScalar(level: Int): Double =
    LevelMultiplier * level / LevelDivider + LevelBoost

  def calculateStabFactor(sourceTypes: List[Type], moveType: Type): Double =
    if sourceTypes.contains(moveType) then StabMultiplier else NoMultiplier

  def calculateTypeEfficacy(moveType: Type, targetTypes: List[Type]): Double =
    TypeUtility.calculateTypeMultiplier(
      moveType.computeEffectiveness(targetTypes)
    )

  def calculateRandomFactor: Double =
    RandomGenerator().nextInt(
      RandomFactorMinBound,
      RandomFactorMaxBound
    ) / PercentageDivider

  def calculateFieldFactor(
      moveType: Type,
      source: BattlePokemon,
      target: BattlePokemon,
      battleState: BattleState
  ): Double =
    val weatherTypeModifiers: Map[Type, Double] =
      battleState.field.weather match
        case Some(w: Weather with TypesModifier) => w.typesModifier
        case _                                   => Map.empty

    val terrainTypeModifiers: Map[Type, Double] =
      battleState.field.terrain match
        case Some(t: Terrain with TypesModifier) => t.typesModifier
        case _                                   => Map.empty

    val roomTypeModifiers: Map[Type, Double] =
      battleState.field.room match
        case Some(r: Room with TypesModifier) => r.typesModifier
        case _                                => Map.empty

    val sidesTypeModifiers: Map[Type, Double] =
      val sourceOwner: Trainer =
        battleState.trainers.find(t => t.inField.get.id == source.id).get
      val sourceOwnerSide: FieldSide = battleState.field.sides(sourceOwner)
      sourceOwnerSide.conditions.collect {
        case sc: SideCondition with TypesModifier => sc.typesModifier
      }.foldLeft(Map[Type, Double]())(_ ++ _)

    val fieldModifiers: Map[Type, Double] =
      weatherTypeModifiers ++ terrainTypeModifiers ++ roomTypeModifiers ++ sidesTypeModifiers

    fieldModifiers.filter((typ, _) => typ == moveType).values.product
