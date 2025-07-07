package it.unibo.skalamon.model.field.room

import it.unibo.skalamon.model.battle.{BattleRule, Tricky}
import it.unibo.skalamon.model.field.FieldEffectMixin
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  MutatedBattleRule,
  Room
}

case class TrickRoom(t: Int) extends Room with FieldEffect(t)
    with Expirable(t, TrickRoom.Duration) with MutatedBattleRule:
  override val description: String = TrickRoom.Description
  override val rule: BattleRule = Tricky()
  override val name: String = TrickRoom.Name

object TrickRoom:
  val Name: String = "Trick Room"
  val Description =
    "Reverses move orders within each priority level, so that slower Pokémon move before faster ones."
  val Duration: Int = 5
  def apply(t: Int): TrickRoom = new TrickRoom(t)
