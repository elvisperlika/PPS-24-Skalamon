package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.GameState.{GameOver, InProgress}
import it.unibo.skalamon.controller.battle.Trainer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GameStateTest extends AnyFlatSpec with should.Matchers:

  "Game state" should "return true if is Game Over" in:
    val state = GameOver(Trainer("ALice", List()))
    state.isGameOver shouldBe true

  it should "return the winner if is Game Over" in:
    val state = GameOver(Trainer("ALice", List()))
    state.getWinner.get shouldBe Trainer("ALice", List())

  it should "return None if game is In Progress" in:
    val state = InProgress
    state.getWinner shouldBe None
