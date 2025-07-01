package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  TypesModifier,
  Weather
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Rain(t: Int) extends Weather with FieldEffect(t)
    with TypesModifier with Expirable(t, Sandstorm.Duration):
  override val description: String = Sandstorm.Description
  override val typesModifier: Map[Type, Double] =
    Map(Water -> Rain.WaterModifier, Fire -> Rain.FireModifier)

object Rain:
  val WaterModifier: Double = 1.5
  val FireModifier: Double = 0.5
  val Description: String = "Rain boosts Water and weakens Fire Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Rain = new Rain(t)
