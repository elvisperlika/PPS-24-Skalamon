package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.field.{
  DamageAllExceptIce,
  Expirable,
  PokemonRule
}
import it.unibo.skalamon.model.types.Type

case class Snow(name: String, turn: Int, elapsedTurn: Int)
    extends Weather(name) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] = Map.empty
  override val fieldEffects: List[PokemonRule] = DamageAllExceptIce :: Nil

object Snow:
  def apply(t: Int): Snow = Snow("Snow", t, 5)
