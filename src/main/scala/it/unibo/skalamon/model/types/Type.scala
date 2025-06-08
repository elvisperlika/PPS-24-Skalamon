package it.unibo.skalamon.model.types

/** Type
  *
  * A move’s type can be neutral (1x), resisted (0.5x), super effective (2x or
  * 4x) or immune (0x) to the target’s types. Effectiveness is summable, Fire is
  * effective against Grass and Bug type, if a Pokémon is Grass and Bug type the
  * Fire attack will be super effective.
  */
trait Type:
  /** Compute effectiveness of a type against another.
    * @param t
    *   is the defensor type
    * @return
    *   effectiveness multiplier
    */
  def computeEffectiveness(t: PokemonType): Double

/** A Pokémon can have one or two Types.
  */
type PokemonType = Type | List[Type]

/** A move can have only one type.
  */
type MoveType = Type
