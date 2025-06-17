package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.weather.Weather
import it.unibo.skalamon.model.field.{
  Expirable,
  FieldEffect,
  Modify,
  PokemonRule
}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Ice, Water}

case class Snow(
    bornTurn: Int,
    elapsedTurn: Int
) extends FieldEffect
    with Weather("Snow")
    with Expirable(bornTurn, elapsedTurn):
  
  override val typesMultiplierMap: Map[Type, Double] = Map(
    Fire -> 1.5,
    Water -> 0.5
  )

  override val fieldEffects: List[PokemonRule] =
    Modify.except(Ice) { p => p.copy(currentHP = p.currentHP - 10) } :: Nil

object Snow:
  /** Create a Sunny weather.
    * @param t
    *   Number of turns in which weather is active.
    * @return
    *   Sunny weather
    */
  def apply(t: Int): Snow = Snow(t, 5)
