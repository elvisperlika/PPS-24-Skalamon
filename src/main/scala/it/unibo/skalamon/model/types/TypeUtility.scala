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

  /** Calculate type multiplier from two Pokémon.
    * @param attacker
    *   Pokémon in attack mode
    * @param defender
    *   Pokémon in defence mode
    * @return
    *   Multiplier
    */
  def calculateTypeMultiplier(
      attacker: Pokemon,
      defender: Pokemon
  ): Double =
    val efficacyList =
      attacker.types.flatMap(_.computeEffectiveness(defender.types))
    efficacyList.foldLeft(1.0)((acc, t) => acc * t.value)
