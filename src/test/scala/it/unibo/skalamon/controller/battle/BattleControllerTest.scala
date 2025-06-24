package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.*
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[BattleController]].
  */
class BattleControllerTest extends AnyFlatSpec with should.Matchers
    with BeforeAndAfterEach:
  private val alice = trainerAlice
  private val bob = trainerBob

  private var battle = Battle(List(alice, bob))
  private var controller = BattleController(battle)

  private def currentStage: TurnStage =
    controller.battle.currentTurn.get.state.stage

  override def beforeEach(): Unit =
    battle = Battle(List(alice, bob))
    controller = BattleController(battle)

  "Battle controller" should "begin with no turn" in:
    controller.battle.currentTurn shouldBe None

  it should "start a turn when asked" in:
    controller.start()
    currentStage shouldBe TurnStage.Started

  it should "advance to waiting for actions stage" in:
    controller.start()
    controller.update()
    currentStage shouldBe TurnStage.WaitingForActions

  it should "not allow action registration in started stage" in:
    controller.start()
    val action = SwitchAction()
    an[IllegalStateException] should be thrownBy controller.registerAction(
      alice,
      action
    )

  it should "register actions from trainers" in:
    controller.start()

    val action1 = SwitchAction()
    val action2 = SwitchAction()

    controller.update()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)

    currentStage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()
    currentStage shouldBe TurnStage.ExecutingActions

  it should "trigger start events" in:
    var eventTriggered = false
    controller.battle.battleEventManager.watch(TurnStageEvents.TurnStarted) { turn =>
      eventTriggered = true
    }

    controller.start()

    eventTriggered shouldBe true
    currentStage shouldBe TurnStage.Started

  it should "trigger action received events" in:
    controller.start()
    var eventTriggered = false
    controller.battle.battleEventManager.watch(TurnStageEvents.ActionsReceived) {
      turn =>
        eventTriggered = true
    }

    val action1 = SwitchAction()
    val action2 = SwitchAction()

    controller.update()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)

    currentStage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()

    eventTriggered shouldBe true
    currentStage shouldBe TurnStage.ExecutingActions

  it should "reset turn after the final stage" in:
    controller.start()
    currentStage shouldBe TurnStage.Started

    controller.update()
    currentStage shouldBe TurnStage.WaitingForActions

    val action1 = SwitchAction()
    val action2 = SwitchAction()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)
    currentStage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()
    currentStage shouldBe TurnStage.ExecutingActions

    controller.update()
    currentStage shouldBe TurnStage.Ended

    controller.update()
    currentStage shouldBe TurnStage.Started

  it should "add new turn" in:
    controller.start()
    battle.currentTurn shouldEqual Some(
      Turn(TurnState.initial(battle.trainers))
    )
