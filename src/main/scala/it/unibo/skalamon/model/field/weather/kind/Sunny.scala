package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.WhenAffect.OnCreation
import it.unibo.skalamon.model.field.weather.{Weather, WhenAffect}
import it.unibo.skalamon.model.field.{Expirable, PokemonRule}
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Sunny(
    name: String,
    turn: Int,
    elapsedTurn: Int,
    whenAffect: WhenAffect
) extends Weather(name, whenAffect) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] =
    Map((Fire -> 1.5), (Water -> 1.5))

  override val fieldEffects: List[PokemonRule] = Nil

object Sunny:
  /** Create a Sunny weather.
    * @param t
    *   Number of turns in which weather is active.
    * @return
    *   Sunny weather
    */
  def apply(t: Int): Sunny = Sunny("Sunny", t, 5, OnCreation)
