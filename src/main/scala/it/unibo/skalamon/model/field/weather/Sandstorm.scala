package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.event.TurnStageEvents.TurnStarted
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  PokemonRules
}
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.types.TypesCollection.{Ground, Rock, Steel}

case class Sandstorm(t: Int) extends FieldEffect(t) with PokemonRules
    with Expirable(t, Sandstorm.Duration):
  override val description: String = Sandstorm.Description
  override val rules: List[(EventType[_], PokemonRule)] =
    (
      TurnStarted,
      Modify.except(Rock, Steel, Ground) { p =>
        p.copy(currentHP = p.currentHP - 10)
      }
    ) :: Nil

object Sandstorm:
  val Description: String = "Sandstorm damages non-Rock, Ground, Steel types."
  val Duration: Int = 5
  def apply(t: Int): Sandstorm = new Sandstorm(t)
