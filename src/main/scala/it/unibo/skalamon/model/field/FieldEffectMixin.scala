package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.types.Type

object FieldEffectMixin:

  trait Rules:
    val onApply: List[PokemonRule]
    val onTurns: List[PokemonRule]

  trait TypesModifier:
    val typesModifier: Map[Type, Double]

  trait FieldEffect:
    val description: String
    val creationTurn: Int

  trait Expirable(creationTurn: Int):
    val duration: Int
    def isExpired(currentTurn: Int): Boolean =
      currentTurn >= creationTurn + duration

  trait Weather extends FieldEffect with Rules with TypesModifier
  trait Terrain extends FieldEffect with Rules with TypesModifier
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
