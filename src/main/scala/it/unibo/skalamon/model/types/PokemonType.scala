package it.unibo.skalamon.model.types

/** Type
  *
  * A move’s type can be neutral (1x), resisted (0.5x), super effective (2x or
  * 4x) or immune (0x) to the target’s types. Effectiveness is summable, Fire is
  * effective against Grass and Bug type, if a Pokémon is Grass and Bug type the
  * Fire attack will be super effective.
  */
trait Type:
  def computeEffectiveness(t: PokemonType): Double

/** A Pokémon can have one or two Types.
  */
type PokemonType = (Type, Type) | Type

/** A move can have only one move.
  */
type MoveType = Type

/** Efficacy encapsulate multipliers.
  * @param value
  */
enum Efficacy:
  case Effective
  case Resisted
  case Ineffective

  def value: Double = this match
    case Effective   => 2.0
    case Resisted    => 0.5
    case Ineffective => 0.0

abstract class TypeImpl extends Type {
  val effectiveAgainst: List[Type]
  val resistedBy: List[Type]
  val ineffectiveAgainst: List[Type]
  private def isEffectiveAgainst(t: Type): Boolean =
    effectiveAgainst.contains(t)
  private def isResistedBy(t: Type): Boolean = resistedBy.contains(t)
  private def isIneffectiveAgainst(t: Type): Boolean =
    ineffectiveAgainst.contains(t)

  override def computeEffectiveness(t: PokemonType): Double =
    t match {
      case t: Type              => computeSingleEffectiveness(t)
      case (t1: Type, t2: Type) =>
        computeSingleEffectiveness(t1) + computeSingleEffectiveness(t2)
    }

  private def computeSingleEffectiveness(t: Type): Double =
    import Efficacy.*
    if isEffectiveAgainst(t) then Effective.value
    else if isResistedBy(t) then Resisted.value
    else if isIneffectiveAgainst(t) then Ineffective.value
    else 1
}

case object Normal extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = Nil
  override val resistedBy: List[MoveType] = List(Rock, Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Ghost)
}

case object Fire extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Grass, Ice, Bug, Steel)
  override val resistedBy: List[MoveType] = List(Fire, Water, Rock, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Water extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Fire, Ground, Rock)
  override val resistedBy: List[MoveType] = List(Water, Grass, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Grass extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Water, Ground, Rock)
  override val resistedBy: List[MoveType] =
    List(Fire, Grass, Poison, Flying, Bug, Dragon, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Electric extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Water, Flying)
  override val resistedBy: List[MoveType] = List(Electric, Grass, Dragon)
  override val ineffectiveAgainst: List[MoveType] = List(Ground)
}

case object Ice extends TypeImpl {
  override val effectiveAgainst: List[MoveType] =
    List(Grass, Ground, Flying, Dragon)
  override val resistedBy: List[MoveType] = List(Fire, Water, Ice, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Fighting extends TypeImpl {
  override val effectiveAgainst: List[MoveType] =
    List(Normal, Ice, Rock, Dark, Steel)
  override val resistedBy: List[MoveType] =
    List(Poison, Flying, Psychic, Bug, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List(Ghost)
}

case object Poison extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Grass, Fairy)
  override val resistedBy: List[MoveType] = List(Poison, Ground, Rock, Ghost)
  override val ineffectiveAgainst: List[MoveType] = List(Steel)
}

case object Ground extends TypeImpl {
  override val effectiveAgainst: List[MoveType] =
    List(Fire, Electric, Poison, Rock, Steel)
  override val resistedBy: List[MoveType] = List(Grass, Bug)
  override val ineffectiveAgainst: List[MoveType] = List(Flying)
}

case object Flying extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Grass, Fighting, Bug)
  override val resistedBy: List[MoveType] = List(Electric, Rock, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Psychic extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Fighting, Poison)
  override val resistedBy: List[MoveType] = List(Psychic, Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Dark)
}

case object Bug extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Grass, Psychic, Dark)
  override val resistedBy: List[MoveType] =
    List(Fire, Fighting, Poison, Flying, Ghost, Steel, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Rock extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Fire, Ice, Flying, Bug)
  override val resistedBy: List[MoveType] = List(Fighting, Ground, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Ghost extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Ghost, Psychic)
  override val resistedBy: List[MoveType] = List(Dark)
  override val ineffectiveAgainst: List[MoveType] = List(Normal)
}

case object Dragon extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Dragon)
  override val resistedBy: List[MoveType] = List(Steel)
  override val ineffectiveAgainst: List[MoveType] = List(Fairy)
}

case object Dark extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Ghost, Psychic)
  override val resistedBy: List[MoveType] = List(Fighting, Dark, Fairy)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Steel extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Ice, Rock, Fairy)
  override val resistedBy: List[MoveType] = List(Fire, Water, Electric, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}

case object Fairy extends TypeImpl {
  override val effectiveAgainst: List[MoveType] = List(Fighting, Dragon, Dark)
  override val resistedBy: List[MoveType] = List(Fire, Poison, Steel)
  override val ineffectiveAgainst: List[MoveType] = List()
}
