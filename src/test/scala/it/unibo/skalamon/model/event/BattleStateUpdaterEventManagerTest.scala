package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  TurnStage,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * Tests for [[EventManager]] updating the current [[BattleState]].
 */
class BattleStateUpdaterEventManagerTest extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  private var battle = Battle(List(trainerAlice, trainerBob))

  override def beforeEach(): Unit =
    battle = Battle(List(trainerAlice, trainerBob))

  "EventManager" should "be able to read the current battle state" in:
    var notified = false

    battle.eventManager.hookBattleStateUpdate(TurnStageEvents.Started): (battleState, _) =>
      notified = true
      battleState.trainers shouldBe battle.trainers
      battleState

    battle.currentTurn shouldBe None

    battle.start()
    notified shouldBe true

  "EventManager" should "be able to update the current battle state" in:
    var notified = false

    battle.eventManager.hookBattleStateUpdate(TurnStageEvents.WaitingForActions): (battleState, _) =>
      notified = true
      battleState.copy(trainers = List.empty)

    battle.start()
    battle.currentTurn.get.state.stage shouldBe TurnStage.Started
    battle.currentTurn.get.state.snapshot.trainers shouldBe battle.trainers

    battle.update()
    notified shouldBe true
    battle.currentTurn.get.state.stage shouldBe TurnStage.WaitingForActions
    battle.currentTurn.get.state.snapshot.trainers shouldBe List.empty

