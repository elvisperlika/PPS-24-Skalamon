package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.Expirable
import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.types.{Fire, Type, Water}

case class Rain(name: String, turn: Int, elapsedTurn: Int)
    extends Weather(name) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] =
    Map((Water -> 1.5), (Fire -> 0.5))

object Rain:
  def apply(t: Int): Rain = Rain("Rain", t, 5)
