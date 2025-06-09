package it.unibo.skalamon.model.types

/** Type
  *
  * A move’s type can be neutral (1x), resisted (0.5x), super effective (2x or
  * 4x) or immune (0x) to the target’s types. Effectiveness is summable, Fire is
  * effective against Grass and Bug type, if a Pokémon is Grass and Bug type the
  * Fire attack will be super effective.
  */
trait Type(
    val superEffectiveAgainst: List[Type],
    val resistedBy: List[Type],
    val ineffectiveAgainst: List[Type]
):

  /** Compute effectiveness of a type against another.
    * @param t
    *   Defensor type
    * @return
    *   Efficacy list
    */
  def computeEffectiveness(t: PokemonType): List[Efficacy] =
    t match
      case t: Type          => List(computeSingleEffectiveness(t))
      case list: List[Type] =>
        list.foldLeft(List[Efficacy]())((list, t) =>
          computeSingleEffectiveness(t) :: list
        )

  private def computeSingleEffectiveness(t: Type): Efficacy =
    import Efficacy.*
    if superEffectiveAgainst contains t then SuperEffective
    else if resistedBy contains t then Resisted
    else if ineffectiveAgainst contains t then Ineffective
    else Effective

/** A Pokémon can have one or two Types.
  */
type PokemonType = Type | List[Type]

/** A move can have only one type.
  */
type MoveType = Type
