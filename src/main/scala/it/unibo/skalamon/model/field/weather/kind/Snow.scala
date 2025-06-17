package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.{Expirable, Modify, PokemonRule}
import it.unibo.skalamon.model.field.weather.WhenAffect.OnCreation
import it.unibo.skalamon.model.field.weather.{Weather, WhenAffect}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Ice

case class Snow(
    name: String,
    turn: Int,
    elapsedTurn: Int,
    whenAffect: WhenAffect
) extends Weather(name, whenAffect) with Expirable(turn, elapsedTurn):

  override val typesMultiplierMap: Map[Type, Double] = Map.empty
  override val fieldEffects: List[PokemonRule] =
    Modify.except(Ice) { p => p.copy(currentHP = p.currentHP - 10) } :: Nil

object Snow:
  def apply(t: Int): Snow = Snow("Snow", t, 5, OnCreation)
