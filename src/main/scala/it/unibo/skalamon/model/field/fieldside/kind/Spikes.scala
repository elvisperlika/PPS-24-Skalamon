package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.battle.turn.BattleEvents.PokemonSwitchIn
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin.{Expirable, PokemonRules}
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.field.fieldside.{Multi, SideCondition}
import it.unibo.skalamon.model.pokemon.Pokemon
import it.unibo.skalamon.model.types.TypesCollection.{Dragon, Flying}
import it.unibo.skalamon.model.data.percent

import scala.reflect.ClassTag

case class Spikes(t: Int)(implicit val classTag: ClassTag[Spikes])
    extends SideCondition with Multi[Spikes](Spikes.Limit) with PokemonRules
    with Expirable(t, Spikes.Duration):
  override val rules: List[(EventType[_], PokemonRule)] =
    (
      PokemonSwitchIn,
      Modify.except(Flying, Dragon)(p =>
        p.copy(currentHP = p.currentHP - (13.percent of p.currentHP))
      )
    ) :: Nil

object Spikes:
  private val Duration: Int = 5
  private val Limit: Int = 3
  def apply(creationTurn: Int)(implicit classTag: ClassTag[Spikes]): Spikes =
    new Spikes(creationTurn)(classTag)
