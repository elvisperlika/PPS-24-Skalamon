package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Ice}

trait PokemonRule:
  def apply(p: BattlePokemon): BattlePokemon

enum FilterEnum:
  case Only
  case Except
  case All

object Modify:
  def only(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.Only, types.toList)
  def except(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.Except, types.toList)
  def all: ModifyBuilder =
    ModifyBuilder(FilterEnum.All, Nil)

case class ModifyBuilder(filter: FilterEnum, types: List[Type]):
  def apply(modification: BattlePokemon => BattlePokemon): PokemonRule =
    (p: BattlePokemon) =>
      val matches = filter match
        case FilterEnum.Only   => types.contains(p.base.types)
        case FilterEnum.Except => !types.contains(p.base.types)
        case _                 => true
      if matches then modification(p) else p
