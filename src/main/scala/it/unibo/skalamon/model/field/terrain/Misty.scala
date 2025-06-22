package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Terrain,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Dragon

case class Misty(t: Int)
    extends Terrain
    with FieldEffect(t)
    with TypesModifier
    with Expirable(t, Misty.Duration):
  override val description: String = Misty.Description
  override val typesModifier: Map[Type, Double] = Map(Dragon -> 0.5)

object Misty:
  val Description: String = "Misty Terrain"
  val Duration: Int = 5
  def apply(t: Int): Misty = new Misty(t)
