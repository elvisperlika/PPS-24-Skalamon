package it.unibo.skalamon.model.field.room

import it.unibo.skalamon.model.field.FieldEffectMixin.BaseRoom

case class TrickRoom(t: Int) extends BaseRoom(
      description = TrickRoom.Description,
      creationTurn = t
    )

object TrickRoom:
  val Description =
    "Reverses move orders within each priority level, so that slower Pokémon move before faster ones."
  def apply(t: Int): TrickRoom = new TrickRoom(t)
