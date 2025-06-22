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
  override val typesModifier: Map[Type, Double] = Map(Fire -> 1.5, Water -> 1.5)
  override val description: String = Mud.Description

object Mud:
  val Description: String = "Mud"
  val Duration: Int = 5
  def apply(t: Int): Mud = new Mud(t)
