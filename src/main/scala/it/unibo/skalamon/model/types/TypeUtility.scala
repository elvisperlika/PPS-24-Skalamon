package it.unibo.skalamon.model.types

import it.unibo.skalamon.model.pokemon.{BattlePokemon, Pokemon}

object TypeUtility:

  /** Calculate multiplier from list of Efficacy.
    * @param typesList
    *   List of Efficacy
    * @return
    *   Multiplier
    */
  def calculateTypeMultiplier(typesList: List[Efficacy]): Double =
    typesList.foldLeft(1.0)((acc, t) => acc * t.value)
