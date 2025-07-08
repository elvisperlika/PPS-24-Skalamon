package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Terrain,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Mud(t: Int)
    extends Terrain
    with FieldEffect(t)
    with TypesModifier
    with Expirable(t, Mud.Duration):
  override val typesModifier: Map[Type, Double] =
    Map(Fire -> Mud.FireModifier, Water -> Mud.WaterModifier)
  override val description: String = Mud.Description
  override val name: String = Mud.Name

object Mud:
  val Name = "Mud"
  val WaterModifier: Double = 1.5
  val FireModifier: Double = 1.5
  val Description: String = "Mud boosts Fire and Water Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Mud = new Mud(t)
