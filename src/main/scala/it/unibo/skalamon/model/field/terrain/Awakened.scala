package it.unibo.skalamon.model.field.terrain

import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Terrain,
  TypesModifier
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Psychic

/** Awakened -> Psychic
  * @param t
  *   Index of creation turn
  */
case class Awakened(t: Int) extends Terrain with FieldEffect(t)
    with TypesModifier with Expirable(t, Awakened.Duration):
  override val description: String = Awakened.Description
  override val typesModifier: Map[Type, Double] = Map(Psychic -> 1.5)

object Awakened:
  val Description: String = "Psychic boosts Psychic Pokémon."
  val Duration: Int = 5
  def apply(t: Int): Awakened = new Awakened(t)
