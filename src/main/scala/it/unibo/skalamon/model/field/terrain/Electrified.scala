package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseTerrain, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.Electric

case class Electrified(t: Int) extends BaseTerrain(
      description = Electrified.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map(Electric -> 1.5)
    ) with Expirable(t):
  override val duration: Int = Electrified.Duration

object Electrified:
  val Description: String = "Electric Terrain"
  val Duration: Int = 5
  def apply(t: Int): Electrified = new Electrified(t)
