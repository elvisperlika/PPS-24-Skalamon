package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.Ice

trait PokemonRule:
  def apply(p: BattlePokemon): BattlePokemon

trait Filter:
  val filter: FilterEnum
  val types: List[Type]

  def shouldApplyTo(p: BattlePokemon): Boolean = filter match
    case FilterEnum.All    => types.contains(p.base.types)
    case FilterEnum.Except => !types.contains(p.base.types)

trait Damage:
  val damage: Int

enum FilterEnum:
  case All
  case Except

object DamageAllExceptIce extends PokemonRule with Filter with Damage:
  override def apply(p: BattlePokemon): BattlePokemon =
    if shouldApplyTo(p) then p.copy(currentHP = p.currentHP - damage) else p

  override val filter: FilterEnum = FilterEnum.Except
  override val types: List[Type] = Ice :: Nil
  override val damage: Int = 10
