package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  TypesModifier,
  Weather
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Sunny(t: Int)
    extends Weather
    with FieldEffect(t)
    with TypesModifier
    with Expirable(t, Sunny.Duration):
  override val typesModifier: Map[Type, Double] =
    Map(Fire -> Sunny.FireModifier, Water -> Sunny.WaterModifier)
  override val description: String = Sunny.Description
  override val name: String = Sunny.Name

object Sunny:
  val Name = "Sunny"
  private val WaterModifier: Double = 1.5
  private val FireModifier: Double = 1.5
  val Description: String = "Sunny burn grass Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Sunny = new Sunny(t)
