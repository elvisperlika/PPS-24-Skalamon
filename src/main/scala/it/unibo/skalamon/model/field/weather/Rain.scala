package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Rain(t: Int) extends FieldEffect(t)
    with TypesModifier with Expirable(t, Sandstorm.Duration):
  override val description: String = Sandstorm.Description
  override val typesModifier: Map[Type, Double] = Map(Water -> 1.5, Fire -> 0.5)

object Rain:
  val Description: String = "Rain boosts Water and weakens Fire Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Rain = new Rain(t)
