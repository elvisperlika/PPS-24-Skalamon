package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.event.TurnStageEvents.Started
import it.unibo.skalamon.model.event.{Event, EventManager, EventType}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Ice

object FieldEffectMixin:

  /** [[FieldEffect]] mixin to add [[PokemonRule]]s to execute when an event is
    * triggered.
    */
  trait PokemonRules:
    /** List of [[Event]]s that trigger [[PokemonRule]]s.
      */
    val rules: List[(EventType[_], PokemonRule)]

  /** [[FieldEffect]] mixin to add [[Type]] modifiers that have to be applied to
    * Pokémon moves with defined types.
    */
  trait TypesModifier:
    /** Map: [[Type]] -> Multiplier/Divider
      */
    val typesModifier: Map[Type, Double]

  /** [[FieldEffect]] represent a dynamic state of the battlefield.
    */
  trait FieldEffect(creationTurn: Int):
    val description: String

  /** [[FieldEffect]] mixin to add duration.
    */
  trait Expirable(creationTurn: Int, duration: Int):

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
  trait Weather

  /** Represents a terrain effect that alters the battlefield. Can modify type
    * interactions and apply rules over time.
    */
  trait Terrain

  /** Represents a special room effect on the battlefield. Does not influence
    * types, but can change other mechanics.
    */
  trait Room
  
  
  trait SideCondition

/* override val description: String = "Sunny"
    override val rules: List[(EventType[_], PokemonRule)] =
      (
        Started,
        Modify.except(Ice) { p => p.copy(currentHP = p.currentHP - 10) }
      ) :: Nil
    override val typesModifier: Map[Type, Double] = Nil
    override val duration: Int =  */
//  abstract class BaseWeather(
//      override val description: String,
//      override val creationTurn: Int,
//      override val typesModifier: Map[Type, Double]
//  ) extends Weather
//
//  abstract class BaseTerrain(
//      override val description: String,
//      override val creationTurn: Int,
//      override val onApply: List[PokemonRule],
//      override val onTurns: List[PokemonRule],
//      override val typesModifier: Map[Type, Double]
//  ) extends Terrain

//  abstract class BaseRoom(
//      override val description: String,
//      override val creationTurn: Int
//  ) extends Room
