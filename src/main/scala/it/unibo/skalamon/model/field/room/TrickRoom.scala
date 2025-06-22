package it.unibo.skalamon.model.field.room

import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  Room
}

case class TrickRoom(t: Int) extends Room with FieldEffect(t)
    with Expirable(t, TrickRoom.Duration):
  override val description: String = TrickRoom.Description

object TrickRoom:
  val Description =
    "Reverses move orders within each priority level, so that slower Pokémon move before faster ones."
  val Duration: Int = 5
  def apply(t: Int): TrickRoom = new TrickRoom(t)
