package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.battle.{
  Battle,
  BattleState,
  TurnStage,
  hookBattleStateUpdate
}
import it.unibo.skalamon.model.behavior.kind.DamageBehavior
import it.unibo.skalamon.model.move.{BattleMove, Move, createContext}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[EventManager]] updating the current [[BattleState]].
  */
class BattleStateUpdaterEventManagerTest extends AnyFlatSpec
    with should.Matchers with BeforeAndAfterEach:
  private var battle = Battle(List(trainerAlice, trainerBob))

  override def beforeEach(): Unit =
    battle = Battle(List(trainerAlice, trainerBob))

  "EventManager" should "be able to read the current battle state" in:
    var notified = false

    battle.hookBattleStateUpdate(TurnStageEvents.Started): (battleState, _) =>
      notified = true
      battleState.trainers shouldBe battle.trainers
      battleState

    battle.currentTurn shouldBe None

    battle.start()
    notified shouldBe true

  it should "be able to update the current battle state" in:
    var notified = false

    battle.hookBattleStateUpdate(TurnStageEvents.WaitingForActions):
      (battleState, _) =>
        notified = true
        battleState.copy(trainers = List.empty)

    battle.start()
    battle.currentTurn.get.state.stage shouldBe TurnStage.Started
    battle.currentTurn.get.state.snapshot.trainers shouldBe battle.trainers

    battle.update()
    notified shouldBe true
    battle.currentTurn.get.state.stage shouldBe TurnStage.WaitingForActions
    battle.currentTurn.get.state.snapshot.trainers shouldBe List.empty

  it should "trigger a state changed event after updating" in:
    var notified = false

    battle.hookBattleStateUpdate(TurnStageEvents.Started): (battleState, _) =>
      battleState.copy(trainers = List.empty)

    battle.eventManager.watch(BattleStateEvents.Changed): (previous, current) =>
      notified = true
      previous.trainers shouldBe battle.trainers
      current.trainers shouldBe List.empty

    battle.start()
    notified shouldBe true

  it should "trigger behavior events" in:
    val behavior = DamageBehavior(10)
    var notified = false

    battle.eventManager.watch(BehaviorEvent[DamageBehavior]()): _ =>
      notified = true

    val move =
      Move(
        "TestMove",
        priority = 5,
        success = behavior
      )
    val context = BattleMove(move, pp = 10).createContext(
      _.success,
      PokemonTestUtils.simplePokemon1,
      PokemonTestUtils.simplePokemon2
    )

    battle.start()

    given EventManager = battle.eventManager
    context(battle.currentTurn.get.state.snapshot)
    notified shouldBe true
