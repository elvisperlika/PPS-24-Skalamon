package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.pokemon.{Pokemon, Stat}
import it.unibo.skalamon.model.types.{PokemonType, Type}

/** A builder for creating Pokémon instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the Pokémon being built.
  */
class PokemonBuilder(private val name: String):
  private var types: Option[PokemonType] = None
  private var weight: Option[Double] = None
  private var stats: Map[Stat, Int] = Map.empty

  /** Sets the type or types of the Pokémon.
    *
    * @param types
    *   The types to assign to the Pokémon.
    * @return
    *   This for chaining.
    */
  def typed(types: PokemonType): PokemonBuilder =
    this.types = Some(types)
    this

  /** Sets the weight of the Pokémon in kilograms.
    *
    * @param weight
    *   The weight in kilograms.
    * @return
    *   This for chaining.
    */
  def weighting(weight: Double): PokemonBuilder =
    this.weight = Some(weight)
    this

  /** Sets a base stat.
    * @param stat
    *   A tuple containing the stat and its value.
    * @return
    *   This for chaining.
    */
  def stat(stat: (Stat, Int)): PokemonBuilder =
    this.stats = this.stats + stat
    this

  /** Builds the Pokémon instance with the provided attributes.
    *
    * @return
    *   The constructed Pokémon instance.
    * @throws IllegalArgumentException
    *   If mandatory attributes are not defined.
    */
  def build: Pokemon =
    Pokemon(
      name = name,
      types = types.getOrElse(
        throw new IllegalArgumentException("Types must be defined")
      ),
      baseStats = Stats(stats),
      ability = Ability("Unknown", Map.empty), // Placeholder for ability
      weightKg = weight.getOrElse(
        throw new IllegalArgumentException("Weight must be defined")
      ),
      possibleMoves = List() // Placeholder for moves
    )

extension (t: Type)
  /** Combines this type with another type to create a composite Pokémon type.
    *
    * @param other
    *   The other type to combine with.
    * @return
    *   A new PokémonType that includes both types.
    */
  def and(other: Type): PokemonType =
    List(t, other)

extension (d: Double)
  /** Syntactic sugar for Pokémon weight in kilograms.
    * @return
    *   This value.
    */
  def kg: Double = d

/** DSL function to create a base Pokémon instance.
  *
  * @param name
  *   The name of the Pokémon.
  * @param build
  *   A function that takes a `PokemonBuilder` and returns a modified builder.
  * @return
  *   The constructed Pokémon instance.
  */
def pokemon(name: String)(build: PokemonBuilder => PokemonBuilder): Pokemon =
  build(PokemonBuilder(name)).build
