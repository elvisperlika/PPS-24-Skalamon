package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseWeather, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Sunny(t: Int) extends BaseWeather(
      description = Sunny.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map((Fire -> 1.5), (Water -> 1.5))
    ) with Expirable(t):
  override val duration: Int = Sunny.Duration

object Sunny:
  val Description: String = "Sunny burn grass Pokémon"
  val Duration: Int = 5

  /** Create a Sunny weather.
    * @param t
    *   Creation turn
    * @return
    *   Sunny weather
    */
  def apply(t: Int): Sunny = new Sunny(t)
