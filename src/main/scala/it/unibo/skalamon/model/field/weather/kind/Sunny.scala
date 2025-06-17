package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.field.{
  Expirable,
  FieldEffect,
  PokemonRule,
  WhenEffect
}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Sunny(
    bornTurn: Int,
    elapsedTurn: Int
) extends FieldEffect
    with Weather("Sunny")
    with Expirable(bornTurn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] = Map(
    Fire -> 1.5,
    Water -> 0.5
  )

  override val fieldEffects: List[PokemonRule] = Nil

object Sunny:
  /** Create a Sunny weather.
    * @param t
    *   Number of turns in which weather is active.
    * @return
    *   Sunny weather
    */
  def apply(t: Int): Sunny = Sunny(t, 5)
