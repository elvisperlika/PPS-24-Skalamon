package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Ice}

trait PokemonRule:
  def apply(p: BattlePokemon): BattlePokemon

enum FilterEnum:
  case All
  case Except

object Modify:
  def only(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.All, types.toList)
  def except(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.Except, types.toList)

case class ModifyBuilder(filter: FilterEnum, types: List[Type]):
  def apply(modification: BattlePokemon => BattlePokemon): PokemonRule =
    (p: BattlePokemon) =>
      val matches = filter match
        case FilterEnum.All    => types.contains(p.base.types)
        case FilterEnum.Except => !types.contains(p.base.types)
      if matches then modification(p) else p
