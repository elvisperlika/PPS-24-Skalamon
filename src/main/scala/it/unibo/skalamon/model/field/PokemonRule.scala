package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Ice}

/** A rule that can be applied to a BattlePokemon to produce a modified version.
  */
trait PokemonRule:
  /** Applies the rule to a given BattlePokemon.
    *
    * @param p
    *   Pokémon to modify
    * @return
    *   Modified Pokémon
    */
  def apply(p: BattlePokemon): BattlePokemon

/** Enum representing how a modification filter is applied to Pokémon types.
  */
enum FilterEnum:
  /** Apply the modification only to Pokémon of the specified types. */
  case Only

  /** Apply the modification to all except the specified types. */
  case Except

  /** Apply the modification to all Pokémon, regardless of type. */
  case All

/** Utility object for building type-based Pokémon modifications.
  */
object Modify:

  /** Applies a modification only to Pokémon with the specified types.
    *
    * @param types
    *   Types to include
    * @return
    *   Builder for the modification
    */
  def only(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.Only, types.toList)

  /** Applies a modification to all Pokémon except those with the specified
    * types.
    *
    * @param types
    *   Types to exclude
    * @return
    *   Builder for the modification
    */
  def except(types: Type*): ModifyBuilder =
    ModifyBuilder(FilterEnum.Except, types.toList)

  /** Applies a modification to all Pokémon, regardless of type.
    *
    * @return
    *   Builder for the modification
    */
  def all: ModifyBuilder =
    ModifyBuilder(FilterEnum.All, Nil)

/** Builder that creates a PokemonRule based on type filters and a modification
  * function.
  *
  * @param filter
  *   Filter strategy to use (only, except, or all)
  * @param types
  *   List of types used for filtering
  */
case class ModifyBuilder(filter: FilterEnum, types: List[Type]):

  /** Builds a PokemonRule using the specified modification function. The rule
    * will apply conditionally based on the filter and types provided.
    *
    * @param modification
    *   Transformation to apply to matching Pokémon
    * @return
    *   PokemonRule that performs the modification
    */
  def apply(modification: BattlePokemon => BattlePokemon): PokemonRule =
    (p: BattlePokemon) =>
      val matches = filter match
        case FilterEnum.Only   => types.contains(p.base.types)
        case FilterEnum.Except => !types.contains(p.base.types)
        case _                 => true
      if matches then modification(p) else p
