package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.model.event.TurnStageEvents.TurnEnded
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.{
  trainerAlice,
  trainerGio
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleEventManagerTest extends AnyFlatSpec with should.Matchers:

  /** NB: trainerGio has all team KO, trainerAlice not
    */

  "Battle Event Manager" should "set game state as InProgress if there isn't a winner or a draw" in:
    val battleInProgress: Battle = Battle(trainerAlice :: trainerAlice :: Nil)
    battleInProgress.start()
    battleInProgress.eventManager.notify(
      TurnEnded of battleInProgress.currentTurn.get
    )
    battleInProgress.gameState shouldEqual InProgress

  it should "set game state as GameOver when there is only on trainer with at least a Pokémon alive" in:
    val battleFinishedWithWinner: Battle =
      Battle(trainerAlice :: trainerGio :: Nil)
    battleFinishedWithWinner.start()
    battleFinishedWithWinner.eventManager.notify(
      TurnEnded of battleFinishedWithWinner.currentTurn.get
    )
    battleFinishedWithWinner.gameState shouldEqual GameOver(Some(trainerAlice))

  it should "set game state as GameOver when there is a draw" in:
    val battleFinishedWithDraw: Battle =
      Battle(trainerGio :: trainerGio :: Nil)
    battleFinishedWithDraw.start()
    battleFinishedWithDraw.eventManager.notify(
      TurnEnded of battleFinishedWithDraw.currentTurn.get
    )
    battleFinishedWithDraw.gameState shouldEqual GameOver(None)
