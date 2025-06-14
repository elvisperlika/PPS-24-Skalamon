package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.Expirable
import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.types.{Fire, Type, Water}

case class Sunny(name: String, turn: Int, elapsedTurn: Int)
    extends Weather(name) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] =
    Map((Fire -> 1.5), (Water -> 1.5))

object Sunny:
  def apply(t: Int): Sunny = Sunny("Sunny", t, 5)
