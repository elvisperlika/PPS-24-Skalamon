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
  override val typesModifier: Map[Type, Double] = Map(Electric -> 1.5)

object Electrified:
  val Description: String = "Electric boosts Electric Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Electrified = new Electrified(t)
