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
  override val typesModifier: Map[Type, Double] =
    Map(Grass -> Grassy.GrassModifier)
  override val name: String = Grassy.Name

object Grassy:
  val Name = "Grassy"
  val GrassModifier: Double = 1.5
  val Description: String = "Grassy boosts Grass Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Grassy = new Grassy(t)
