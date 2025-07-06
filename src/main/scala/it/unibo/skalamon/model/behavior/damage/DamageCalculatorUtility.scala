package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.{BattleState, Trainer}
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
import it.unibo.skalamon.model.types.TypesCollection.Electric
import it.unibo.skalamon.model.types.{Type, TypeUtility}

import scala.util.Random

object DamageCalculatorUtility:

  private val NoMultiplier: Double = 1
  private val LevelMultiplier: Double = 2
  private val LevelDivider: Double = 5
  private val LevelBoost: Double = 2
  private val StabMultiplier: Double = 1.5
  private val RandomFactorMinBound: Int = 85
  private val RandomFactorMaxBound: Int = 100
  private val PercentageDivider: Int = 100

  /** Calculate the category scalar based on move's category.
    * @param battleMove
    *   Move being performed
    * @param sourceStat
    *   Statistics of the attacking Pokémon
    * @param targetStat
    *   Statistics of the defending Pokémon
    * @return
    *   The appropriate scalar based on move category: calculated value if
    *   Physical or Special, 1 otherwise
    */
  def calculateCategoryScalar(
      battleMove: BattleMove,
      sourceStat: Stats,
      targetStat: Stats
  ): Double =
    battleMove.move.category match
      case Category.Physical =>
        sourceStat.base(Attack).toDouble / targetStat.base(Defense).toDouble
      case Category.Special =>
        sourceStat.base(SpecialAttack).toDouble / targetStat.base(
          SpecialDefense
        ).toDouble
      case _ => NoMultiplier

  /** Calculates the level scalar based on the attacking Pokémon's level.
    * @param level
    *   The level of the attacking [[BattlePokemon]]
    * @return
    *   A scalar value computed using the level formula
    */
  def calculateLevelScalar(level: Int): Double =
    LevelMultiplier * level / LevelDivider + LevelBoost

  /** Calculates the STAB (Same Type Attack Bonus) factor.
    * @param pokemonTypes
    *   Types of the attacking Pokémon
    * @param moveType
    *   Type of the move being used
    * @return
    *   The STAB multiplier if the move type matches one of the Pokémon's types,
    *   a neutral multiplier otherwise
    */
  def calculateStabFactor(pokemonTypes: List[Type], moveType: Type): Double =
    if pokemonTypes.contains(moveType) then StabMultiplier else NoMultiplier

  /** Calculates the type effectiveness multiplier of a move against a target.
    * @param moveType
    *   Type of the move being used
    * @param targetTypes
    *   Types of the defending Pokémon
    * @return
    *   A multiplier representing the move's effectiveness (e.g., 2.0, 0.5,
    *   etc.).
    */
  def calculateTypeEfficacy(moveType: Type, targetTypes: List[Type]): Double =
    TypeUtility.calculateTypeMultiplier(
      moveType.computeEffectiveness(targetTypes)
    )

  /** Generates a random factor used in damage calculation.
    * @return
    *   A random multiplier between predefined percentage bounds.
    */
  def calculateRandomFactor: Double =
    (Random.nextInt(
      RandomFactorMaxBound
    ) + RandomFactorMinBound).toDouble / PercentageDivider.toDouble

  /** Calculates the field-related type modifier affecting the move.
    *
    * Considers effects from weather, terrain, room, and side conditions that
    * apply type-based modifiers.
    *
    * @param moveType
    *   Type of the move being used
    * @param source
    *   The attacking [[BattlePokemon]]
    * @param target
    *   The defending [[BattlePokemon]]
    * @param battleState
    *   The current battle state
    * @return
    *   A multiplier based on active field effects that influence the move's
    *   type
    */
  def calculateFieldFactor(
      moveType: Type,
      source: BattlePokemon,
      target: BattlePokemon,
      battleState: BattleState
  ): Double =
    // Extract modifiers from weather if it implements TypesModifier
    val weatherTypeModifiers: Map[Type, Double] =
      battleState.field.weather match
        case Some(w: Weather with TypesModifier) => w.typesModifier
        case _                                   => Map.empty

    // Extract modifiers from terrain if it implements TypesModifier
    val terrainTypeModifiers: Map[Type, Double] =
      battleState.field.terrain match
        case Some(t: Terrain with TypesModifier) => t.typesModifier
        case _                                   => Map.empty

    // Extract modifiers from room if it implements TypesModifier
    val roomTypeModifiers: Map[Type, Double] =
      battleState.field.room match
        case Some(r: Room with TypesModifier) => r.typesModifier
        case _                                => Map.empty

    // Extract modifiers from side conditions affecting the source's side
    val sidesTypeModifiers: Map[Type, Double] =
      val sourceOwner: Trainer =
        battleState.trainers.find(_.inField.contains(source)).get
      val sourceOwnerSide: FieldSide =
        battleState.field.sides.find((t, _) => t.id == sourceOwner.id).get._2
      // Combine all type modifiers from applicable side conditions
      sourceOwnerSide.conditions.collect {
        case sc: SideCondition with TypesModifier => sc.typesModifier
      }.foldLeft(Map[Type, Double]())(_ ++ _)

    // Merge all field-based type modifiers
    val fieldModifiers: Map[Type, Double] =
      weatherTypeModifiers ++ terrainTypeModifiers ++ roomTypeModifiers ++ sidesTypeModifiers

    // Filter only modifiers that apply to the moveType, and combine them multiplicatively
    fieldModifiers.filter((typ, _) => typ == moveType).values.product
