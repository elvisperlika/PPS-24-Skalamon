package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.field.{Expirable, FieldEffect, PokemonRule}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Rain(
    bornTurn: Int,
    elapsedTurn: Int
) extends FieldEffect
    with Weather("Rain")
    with Expirable(bornTurn, elapsedTurn):
  
  override val typesMultiplierMap: Map[Type, Double] = Map.empty
  
  override val fieldEffects: List[PokemonRule] = Nil

object Rain:
  def apply(t: Int): Rain = Rain(t, 5)
