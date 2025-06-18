package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.types.Type

object FieldEffectMixin:

  /** [[FieldEffect]] mixin to add [[PokemonRule]]s to execute on applying of
    * the [[FieldEffect]] and to execute at every turn until the [[FieldEffect]]
    * is expired.
    */
  trait Rules:
    /** [[PokemonRule]]s to be executed on [[FieldEffect]] creation turn.
      */
    val onApply: List[PokemonRule]

    /** [[PokemonRule]]s to be executed untile [[FieldEffect]] is expired.
      */
    val onTurns: List[PokemonRule]

  /** [[FieldEffect]] mixin to add [[Type]] modifiers that have to be applied to
    * Pokémon moves with defined types.
    */
  trait TypesModifier:
    /** Map: [[Type]] -> Multiplier/Divider
      */
    val typesModifier: Map[Type, Double]

  /** [[FieldEffect]] represent a dynamic state of the battlefield.
    */
  trait FieldEffect:
    val description: String
    val creationTurn: Int

  /** [[FieldEffect]] mixin to add duration.
    */
  trait Expirable(creationTurn: Int):
    val duration: Int

    /** Checks if the field effect has expired based on the current turn.
      *
      * @param currentTurn
      *   Current turn number of the battle
      * @return
      *   True if the effect's duration has passed; false otherwise
      */
    def isExpired(currentTurn: Int): Boolean =
      currentTurn >= creationTurn + duration

  /** Represents a weather condition affecting the battlefield. Influences rules
    * and type effectiveness.
    */
  trait Weather extends FieldEffect with Rules with TypesModifier

  /** Represents a terrain effect that alters the battlefield. Can modify type
    * interactions and apply rules over time.
    */
  trait Terrain extends FieldEffect with Rules with TypesModifier

  /** Represents a special room effect on the battlefield. Does not influence
    * types, but can change other mechanics.
    */
  trait Room extends FieldEffect

  abstract class BaseWeather(
      override val description: String,
      override val creationTurn: Int,
      override val onApply: List[PokemonRule],
      override val onTurns: List[PokemonRule],
      override val typesModifier: Map[Type, Double]
  ) extends Weather

  abstract class BaseTerrain(
      override val description: String,
      override val creationTurn: Int,
      override val onApply: List[PokemonRule],
      override val onTurns: List[PokemonRule],
      override val typesModifier: Map[Type, Double]
  ) extends Terrain

  abstract class BaseRoom(
      override val description: String,
      override val creationTurn: Int
  ) extends Room
