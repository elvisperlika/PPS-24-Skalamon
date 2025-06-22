package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.event.EventManager
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
  override val typesModifier: Map[Type, Double] = Map(Fire -> 1.5, Water -> 1.5)
  override val description: String = Sunny.Description

object Sunny:
  val Description: String = "Sunny burn grass Pokémon."
  val Duration: Int = 5
  def apply(t: Int, em: EventManager): Sunny = new Sunny(t)
