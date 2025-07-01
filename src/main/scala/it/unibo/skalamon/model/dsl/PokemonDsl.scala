package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.pokemon.{Pokemon, Stat}
import it.unibo.skalamon.model.types.Type

/** A builder for creating Pokémon instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the Pokémon being built.
  */
class PokemonBuilder(private val name: String) extends DslBuilder[Pokemon]:
  private var types: List[Type] = List.empty
  private var hp: Option[Int] = None
  private var weight: Option[Double] = None
  private var stats: Map[Stat, Int] = Map.empty
  private var moves: List[Move] = List.empty
  private var ability: Option[Ability] = None

  /** Sets the type of the Pokémon.
    *
    * @param pokemonType
    *   The single type to assign to the Pokémon.
    * @return
    *   This for chaining.
    */
  def typed(pokemonType: Type): PokemonBuilder =
    this.types = List(pokemonType)
    this

  /** Sets the types of the Pokémon.
    *
    * @param pokemonTypes
    *   The types to assign to the Pokémon.
    * @return
    *   This for chaining.
    */
  def typed(pokemonTypes: List[Type]): PokemonBuilder =
    this.types = pokemonTypes
    this

  /** Sets the HP (Health Points) of the Pokémon.
    * @param hp
    *   The base HP value to assign to the Pokémon.
    * @return
    *   This for chaining.
    * @throws IllegalArgumentException
    *   If the HP is not a positive integer.
    */
  def hp(hp: Int): PokemonBuilder =
    require(hp > 0, "HP must be a positive integer")
    this.hp = Some(hp)
    this

  /** Sets the weight of the Pokémon in kilograms.
    *
    * @param weight
    *   The weight in kilograms.
    * @return
    *   This for chaining.
    */
  def weighing(weight: Double): PokemonBuilder =
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

  /** Sets the moves of the Pokémon.
    *
    * @param moves
    *   The list of moves to assign to the Pokémon.
    * @return
    *   This for chaining.
    */
  def moves(moves: Move*): PokemonBuilder =
    this.moves = moves.toList
    this

  /** Sets the ability of the Pokémon.
    *
    * @param ability
    *   The ability to assign to the Pokémon.
    * @return
    *   This for chaining.
    */
  def ability(ability: Ability): PokemonBuilder =
    this.ability = Some(ability)
    this

  /** Builds the Pokémon instance with the provided attributes.
    *
    * @return
    *   The constructed Pokémon instance.
    * @throws IllegalArgumentException
    *   If mandatory attributes are not defined.
    */
  override def build: Pokemon =
    Pokemon(
      name = name,
      types = Some(types).filterNot(_.isEmpty).getOrElse(
        throw new IllegalArgumentException("At least one type must be defined")
      ),
      hp = hp.getOrElse(
        throw new IllegalArgumentException("HP must be defined")
      ),
      stats = Stats(stats),
      ability = this.ability.getOrElse(
        throw new IllegalArgumentException("Ability must be defined")
      ),
      weightKg = weight.getOrElse(
        throw new IllegalArgumentException("Weight must be defined")
      ),
      moves = this.moves
    )

extension (t: Type)
  /** Combines this type with another type to create a composite Pokémon type.
    *
    * @param other
    *   The other type to combine with.
    * @return
    *   A new PokémonType that includes both types.
    */
  def +(other: Type): List[Type] =
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
