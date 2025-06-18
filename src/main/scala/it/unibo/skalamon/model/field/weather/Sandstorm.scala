package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.field.FieldEffectMixin.{BaseWeather, Expirable}
import it.unibo.skalamon.model.field.Modify
import it.unibo.skalamon.model.types.TypesCollection.{Ground, Rock, Steel}

case class Sandstorm(t: Int) extends BaseWeather(
      description = Sandstorm.Description,
      creationTurn = t,
      onApply = Nil,
      onTurns = Modify.except(Rock, Steel, Ground) { p =>
        p.copy(currentHP = p.currentHP - 10)
      } :: Nil,
      typesModifier = Map.empty
    ) with Expirable(t):
  override val duration: Int = Sandstorm.Duration

object Sandstorm:
  val Description: String = "Sandstorm damages non-Rock, Ground, Steel types"
  val Duration: Int = 5
  def apply(t: Int): Sandstorm = new Sandstorm(t)
