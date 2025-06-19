package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseTerrain, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.Grass

case class Grassy(t: Int) extends BaseTerrain(
      description = Grassy.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map(Grass -> 1.5)
    ) with Expirable(t):
  override val duration: Int = Grassy.Duration

object Grassy:
  val Description: String = "Grassy Terrain"
  val Duration: Int = 5
  def apply(t: Int): Grassy = new Grassy(t)
