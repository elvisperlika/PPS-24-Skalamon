package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.battle.BattleRule
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.types.Type

object FieldEffectMixin:

  /** [[FieldEffect]] mixin to add [[PokemonRule]]s to execute when an event is
    * triggered.
    */
  trait Hooks:
    /** List of [[Event]]s that trigger [[PokemonRule]]s.
      */
    val hooks: List[(EventType[_], PokemonRule)]

  /** [[FieldEffect]] mixin to add [[Type]] modifiers that have to be applied to
    * Pokémon moves with defined types.
    */
  trait TypesModifier:
    /** Map: [[Type]] -> Multiplier/Divider
      */
    val typesModifier: Map[Type, Double]

  /** Represent a dynamic state of the battlefield.
    * @param creationTurn
    *   Creation turn of the field effect
    */
  trait FieldEffect(val creationTurn: Int):
    /** Name of the field effect.
      */
    val name: String
    /** Field effect description.
      */
    val description: String

  /** Represent a battle rule that affect the game.
    */
  trait MutatedBattleRule:
    /** Rule to be setted.
      */
    val rule: BattleRule

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

  /** Represent an effect that afflict only one side of the battlefield,
    * basically only one Pokémon.
    */
  trait SideCondition
