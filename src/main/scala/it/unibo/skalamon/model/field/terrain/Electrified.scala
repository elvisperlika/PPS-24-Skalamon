package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Terrain,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Electric

case class Electrified(t: Int) extends Terrain with FieldEffect(t)
    with TypesModifier with Expirable(t, Electrified.Duration):
  override val description: String = Electrified.Description
  override val typesModifier: Map[Type, Double] =
    Map(Electric -> Electrified.ElectricModifier)
  override val name: String = Electrified.Name

object Electrified:
  val Name = "Electrified"
  val ElectricModifier: Double = 1.5
  val Description: String = "Electric boosts Electric Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Electrified = new Electrified(t)
