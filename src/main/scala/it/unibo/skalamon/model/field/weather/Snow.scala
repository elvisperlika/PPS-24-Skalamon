package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.battle.turn.BattleEvents.CreateWeather
import it.unibo.skalamon.model.event.{EventManager, EventType}
import it.unibo.skalamon.model.field.FieldEffectMixin.*
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.types.TypesCollection.Ice

case class Snow(t: Int)
    extends Weather
    with FieldEffect(t)
    with PokemonRules
    with Expirable(t, Snow.Duration):
  override val description: String = Snow.Description
  override val rules: List[(EventType[_], PokemonRule)] =
    (
      CreateWeather,
      Modify.except(Ice) { p => p.copy(currentHP = p.currentHP - Snow.Damage) }
    ) :: Nil

object Snow:
  val Damage: Int = 10
  val Description: String = "Snow boosts Ice-type defense."
  val Duration: Int = 5
  def apply(t: Int): Snow = new Snow(t)
