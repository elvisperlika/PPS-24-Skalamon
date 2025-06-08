package it.unibo.skalamon.model.types

abstract class TypeImpl extends Type {
  val superEffectiveAgainst: List[Type]
  val resistedBy: List[Type]
  val ineffectiveAgainst: List[Type]
  private def isSuperEffectiveAgainst(t: Type): Boolean =
    superEffectiveAgainst.contains(t)
  private def isResistedBy(t: Type): Boolean = resistedBy.contains(t)
  private def isIneffectiveAgainst(t: Type): Boolean =
    ineffectiveAgainst.contains(t)

  override def computeEffectiveness(t: PokemonType): Double =
    t match
      case t: Type          => computeSingleEffectiveness(t)
      case list: List[Type] =>
        list.foldLeft(1.0)((acc, t) => acc * computeSingleEffectiveness(t))

  private def computeSingleEffectiveness(t: Type): Double =
    import Efficacy.*
    if isSuperEffectiveAgainst(t) then SuperEffective.value
    else if isResistedBy(t) then Resisted.value
    else if isIneffectiveAgainst(t) then Ineffective.value
    else Effective.value
}
