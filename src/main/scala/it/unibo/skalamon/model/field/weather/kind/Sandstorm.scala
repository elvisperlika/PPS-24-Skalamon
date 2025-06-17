package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.{Expirable, FieldEffect, PokemonRule}
import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.types.Type

case class Sandstorm(
    bornTurn: Int,
    elapsedTurn: Int
) extends FieldEffect
    with Weather("Sandstorm")
    with Expirable(bornTurn, elapsedTurn):
  
  override val typesMultiplierMap: Map[Type, Double] = Map.empty
  
  override val fieldEffects: List[PokemonRule] = Nil

object Sandstorm:
  def apply(t: Int): Sandstorm = Sandstorm(t, 5)
