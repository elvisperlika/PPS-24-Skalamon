package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseTerrain, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.Dragon

case class Misty(t: Int) extends BaseTerrain(
      description = Misty.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map(Dragon -> 0.5)
    ) with Expirable(t):
  override val duration: Int = Misty.Duration

object Misty:
  val Description: String = "Misty Terrain"
  val Duration: Int = 5
  def apply(t: Int): Misty = new Misty(t)
