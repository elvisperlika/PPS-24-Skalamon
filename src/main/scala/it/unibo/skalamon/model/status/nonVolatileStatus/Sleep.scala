package it.unibo.skalamon.model.status.nonVolatileStatus

import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.{AssignedStatus, NonVolatileStatus}

/** Pokémon cannot act for 3 turns.
  * @param turnsLeft
  *   The number of turns left for the sleep status to expire. Defaults to
  *   Sleep.SleepTurns.
  */
case class Sleep(turnsLeft: Int = Sleep.DefaultTurns) extends NonVolatileStatus:
  override def executeEffect(pokemon: BattlePokemon): BattlePokemon =
    if turnsLeft > 1 then
      pokemon.copy(
        skipsCurrentTurn = true,
        nonVolatileStatus =
          pokemon.nonVolatileStatus.map(_.copy(status = Sleep(turnsLeft - 1)))
      )
    else
      pokemon.copy(nonVolatileStatus = None, skipsCurrentTurn = false)

object Sleep:
  val DefaultTurns: Int = 3
