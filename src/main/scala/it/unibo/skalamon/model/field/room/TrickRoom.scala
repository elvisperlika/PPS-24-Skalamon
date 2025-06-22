package it.unibo.skalamon.model.field.room

import it.unibo.skalamon.model.battle.turn.BattleEvents.CreateRoom
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.{FieldEffectMixin, Modify, PokemonRule}
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Room,
  PokemonRules
}

case class TrickRoom(t: Int) extends Room with FieldEffect(t)
    with Expirable(t, TrickRoom.Duration):
  override val description: String = TrickRoom.Description

object TrickRoom:
  val Description =
    "Reverses move orders within each priority level, so that slower Pokémon move before faster ones."
  val Duration: Int = 5
  def apply(t: Int): TrickRoom = new TrickRoom(t)
