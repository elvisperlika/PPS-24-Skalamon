package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.battle.turn.BattleEvents.PokemonSwitchIn
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin.{Expirable, FieldEffect, PokemonRules, SideCondition}
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.field.fieldside.Multi
import it.unibo.skalamon.model.pokemon.Pokemon
import it.unibo.skalamon.model.types.TypesCollection.{Dragon, Flying}
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.field.fieldside.kind.Spikes.DamagePercentage

import scala.reflect.ClassTag

case class Spikes(t: Int)(implicit val classTag: ClassTag[Spikes])
    extends SideCondition with Multi[Spikes](Spikes.Limit) with PokemonRules
    with FieldEffect(t)
    with Expirable(t, Spikes.Duration):
  override val rules: List[(EventType[_], PokemonRule)] =
    (
      PokemonSwitchIn,
      Modify.except(Flying, Dragon)(p =>
        p.copy(currentHP = p.currentHP - (DamagePercentage.percent of p.currentHP))
      )
    ) :: Nil
  override val description: String = Spikes.Description

object Spikes:
  val DamagePercentage: Int = 13
  val Description: String =
    "Inflicts cumulative damage equal to 13% of the grounded Pokémon's HP when they switch in."
  val Duration: Int = 5
  private val Limit: Int = 3
  def apply(creationTurn: Int)(implicit classTag: ClassTag[Spikes]): Spikes =
    new Spikes(creationTurn)(classTag)
