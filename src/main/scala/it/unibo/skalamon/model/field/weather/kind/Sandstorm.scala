package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.field.{Expirable, PokemonRule}
import it.unibo.skalamon.model.types.Type

case class Sandstorm(name: String, turn: Int, elapsedTurn: Int)
    extends Weather(name) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] = Map.empty
  override val fieldEffects: List[PokemonRule] = Nil

object Sandstorm:
  def apply(t: Int): Sandstorm = Sandstorm("Sandstorm", t, 5)
