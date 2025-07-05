package it.unibo.skalamon.model.status.volatileStatus

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.VolatileStatus

/** After 1 turn, the target Pokémon will fall asleep, unless switched out. */
case class Yawn() extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 1):
  override def executeEffect(
      pokemon: BattlePokemon,
      currentTurn: Int,
      assignedTurn: Int
  ): BattlePokemon =
    if (currentTurn >= assignedTurn + Yawn.TurnsToActivate) then
      pokemon.copy(skipsCurrentTurn = true)
    else pokemon

object Yawn:
  val TurnsToActivate = 1
