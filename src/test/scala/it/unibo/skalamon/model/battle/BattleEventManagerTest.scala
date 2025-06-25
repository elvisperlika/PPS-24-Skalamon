package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.{
  simplePokemon1ko,
  trainerAlice
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleEventManagerTest extends AnyFlatSpec with should.Matchers:

  def trainerAsh: Trainer =
    Trainer("Ash", simplePokemon1ko :: Nil)

  /** NB: trainerAsh has all team KO, trainerAlice not
    */

  "Battle Event Manager" should "set game state as InProgress if there isn't a winner or a draw" in:
    val battleInProgress: Battle = Battle(trainerAlice :: trainerAlice :: Nil)
    battleInProgress.start()
    battleInProgress.eventManager.notify(
      Ended of battleInProgress.currentTurn.get
    )
    battleInProgress.gameState shouldEqual InProgress

  it should "set game state as GameOver when there is only on trainer with at least a Pokémon alive" in:
    val battleFinishedWithWinner: Battle =
      Battle(trainerAlice :: trainerAsh :: Nil)
    battleFinishedWithWinner.start()
    battleFinishedWithWinner.eventManager.notify(
      Ended of battleFinishedWithWinner.currentTurn.get
    )
    battleFinishedWithWinner.gameState shouldEqual GameOver(Some(trainerAlice))

  it should "set game state as GameOver when there is a draw" in:
    val battleFinishedWithDraw: Battle =
      Battle(trainerAsh :: trainerAsh :: Nil)
    battleFinishedWithDraw.start()
    battleFinishedWithDraw.eventManager.notify(
      Ended of battleFinishedWithDraw.currentTurn.get
    )
    battleFinishedWithDraw.gameState shouldEqual GameOver(None)
