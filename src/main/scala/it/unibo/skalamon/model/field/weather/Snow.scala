package it.unibo.skalamon.model.field.weather

import it.unibo.skalamon.model.battle.turn.BattleEvents.CreateWeather
import it.unibo.skalamon.model.event.{EventManager, EventType}
import it.unibo.skalamon.model.field.FieldEffectMixin.*
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.types.TypesCollection.Ice

case class Snow(t: Int)
    extends Weather
    with FieldEffect(t)
    with Hooks
    with Expirable(t, Snow.Duration):
  override val description: String = Snow.Description
  override val hooks: List[(EventType[_], PokemonRule)] =
    (
      CreateWeather,
      Modify.except(Ice) { p => p.copy(currentHP = p.currentHP - Snow.Damage) }
    ) :: Nil
  override val name: String = Snow.Name

object Snow:
  val Name = "Snow"
  val Damage: Int = 10
  val Description: String = "Snow boosts Ice-type defense."
  val Duration: Int = 5
  def apply(t: Int): Snow = new Snow(t)
