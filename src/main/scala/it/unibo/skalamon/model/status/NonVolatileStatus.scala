package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.{BattlePokemon, Stat}

trait NonVolatileStatus extends Status:
  def executeEffect(
      pokemon: BattlePokemon
  ): BattlePokemon

case object Paralyze extends NonVolatileStatus
/** Inflicts damage equal to 1/16 of the maximum HP and halves the physical
  * Attack stat.
  */
case object Burn extends NonVolatileStatus:
  val BurnAttackReduction = 2
  val BurnDamageReduction = 16

case object Sleep extends NonVolatileStatus

case object Freeze extends NonVolatileStatus

case object Poison extends NonVolatileStatus

case object BadlyPoison extends NonVolatileStatus
