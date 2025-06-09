package it.unibo.skalamon.model.types

object TypeUtility:

  /** Calculate multiplier from list of Efficacy.
    * @param typesList
    *   List of Efficacy
    * @return
    *   Multiplier
    */
  def calculateMultiplier(typesList: List[Efficacy]): Double =
    typesList.foldLeft(1.0)((acc, t) => acc * t.value)
