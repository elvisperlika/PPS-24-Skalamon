package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Terrain,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Grass

case class Grassy(t: Int) extends Terrain with FieldEffect(t) with TypesModifier
    with Expirable(t, Grassy.Duration):
  override val description: String = Grassy.Description
  override val typesModifier: Map[Type, Double] = Map(Grass -> 1.5)

object Grassy:
  val Description: String = "Grassy Terrain"
  val Duration: Int = 5
  def apply(t: Int): Grassy = new Grassy(t)
