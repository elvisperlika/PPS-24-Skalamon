package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.GameState.InProgress
import it.unibo.skalamon.model.event.TurnStageEvents.Ended
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.{
  simplePokemon1ko,
  trainerAlice
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleEventManagerTest extends AnyFlatSpec with should.Matchers:

  /** NB: trainerAsh has all team KO, trainerAlice not
   */
  def trainerAsh: Trainer =
    Trainer("Ash", simplePokemon1ko :: Nil)

  "Battle Event Manager" should "set game state as InProgress if there isn't a winner or a draw" in:
    val battleInProgress: Battle = Battle(trainerAlice :: trainerAlice :: Nil)
    battleInProgress.start()
    battleInProgress.eventManager.notify(
      Ended of battleInProgress.currentTurn.get
    )
    battleInProgress.gameState shouldEqual InProgress
