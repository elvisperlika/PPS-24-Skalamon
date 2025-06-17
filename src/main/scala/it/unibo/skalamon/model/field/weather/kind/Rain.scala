package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.WhenAffect.EveryTurn
import it.unibo.skalamon.model.field.weather.{Weather, WhenAffect}
import it.unibo.skalamon.model.field.{Expirable, PokemonRule}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Rain(name: String, turn: Int, elapsedTurn: Int, whenAffect: WhenAffect)
    extends Weather(name, whenAffect) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] =
    Map((Water -> 1.5), (Fire -> 0.5))
  override val fieldEffects: List[PokemonRule] = Nil

object Rain:
  def apply(t: Int): Rain = Rain("Rain", t, 5, EveryTurn)
