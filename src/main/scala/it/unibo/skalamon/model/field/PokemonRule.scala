package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.Filter.Except
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.types.TypesCollection.Ice
import it.unibo.skalamon.model.types.Type

trait PokemonRule:
  def apply(p: BattlePokemon): BattlePokemon

enum Filter:
  case All
  case Except

class DamageTypesWithFilter(
    val damage: Int,
    val filter: Filter,
    val types: Type*
) extends PokemonRule:
  private def shouldApplyTo(p: BattlePokemon): Boolean = filter match
    case Filter.All    => types.contains(p.base.types)
    case Filter.Except => !types.contains(p.base.types)
  override def apply(p: BattlePokemon): BattlePokemon =
    if shouldApplyTo(p) then p.copy(currentHP = p.currentHP - damage)
    else p

object DamageAllExceptIce
    extends DamageTypesWithFilter(
      damage = 10,
      filter = Except,
      types = Ice
    )
