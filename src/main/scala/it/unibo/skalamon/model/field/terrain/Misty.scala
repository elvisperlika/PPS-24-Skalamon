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
  override val typesModifier: Map[Type, Double] =
    Map(Dragon -> Misty.DragonModifier)
  override val name: String = Misty.Name

object Misty:
  val Name = "Misty"
  val DragonModifier: Double = 0.5
  val Description: String = "Misty weakens Dragon Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Misty = new Misty(t)
