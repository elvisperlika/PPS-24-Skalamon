package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseWeather, Expirable}
import it.unibo.skalamon.model.types.TypesCollection.{Fire, Water}

case class Rain(t: Int) extends BaseWeather(
      description = Rain.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Nil,
      typesModifier = Map(Water -> 1.5, Fire -> 0.5)
    ) with Expirable(t):
  override val duration: Int = Rain.Duration

object Rain:
  val Description: String = "Rain boosts Water, weakens Fire"
  val Duration: Int = 5
  def apply(t: Int): Rain = new Rain(t)
