package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseTerrain, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.Psychic

// Awakened -> Psychic
case class Awakened(t: Int) extends BaseTerrain(
      description = Awakened.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map(Psychic -> 1.5)
    ) with Expirable(t):
  override val duration: Int = Awakened.Duration

object Awakened:
  val Description: String = "Psychic Terrain"
  val Duration: Int = 5
  def apply(t: Int): Awakened = new Awakened(t)
