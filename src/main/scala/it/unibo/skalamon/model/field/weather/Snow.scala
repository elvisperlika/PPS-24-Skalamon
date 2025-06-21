package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseWeather, Expirable}
import it.unibo.skalamon.model.field.Modify
import it.unibo.skalamon.model.types.TypesCollection.Ice

case class Snow(t: Int) extends BaseWeather(
      description = Snow.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Modify.except(Ice) { p =>
        p.copy(currentHP = p.currentHP - 10)
      } :: Nil,
      typesModifier = Map.empty
    ) with Expirable(t):
  override val duration: Int = Snow.Duration

object Snow:
  val Description: String = "Snow boosts Ice-type defense"
  val Duration: Int = 5
  def apply(t: Int): Snow = new Snow(t)
