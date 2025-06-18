package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseTerrain, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Mud(t: Int) extends BaseTerrain(
      description = Mud.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map((Fire -> 1.5), (Water -> 1.5))
    ) with Expirable(t):
  override val duration: Int = Mud.Duration

object Mud:
  val Description: String = "Mud"
  val Duration: Int = 5
  def apply(t: Int): Mud = new Mud(t)
