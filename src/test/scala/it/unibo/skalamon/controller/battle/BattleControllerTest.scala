package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.{Battle, TurnStage}
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[BattleController]].
  */
class BattleControllerTest extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  private val alice = trainerAlice
  private val bob = trainerBob

  private var battle = Battle(List(alice, bob))
  private var controller = BattleController(battle)

  override def beforeEach(): Unit = {
    battle = Battle(List(alice, bob))
    controller = BattleController(battle)
  }

  "Battle controller" should "begin with no turn" in:
    controller.battle.turn shouldBe None

  it should "start a turn when asked" in:
    controller.start()
    controller.battle.turn.get.state.stage shouldBe TurnStage.Started

  it should "advance to waiting for actions stage" in:
    controller.start()
    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.WaitingForActions

  it should "not allow action registration in started stage" in:
    controller.start()
    val action = SwitchAction()
    an[IllegalStateException] should be thrownBy controller.registerAction(alice, action)

  it should "register actions from trainers" in:
    controller.start()

    val action1 = SwitchAction()
    val action2 = SwitchAction()

    controller.update()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)

    controller.battle.turn.get.state.stage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()

    controller.battle.turn.get.state.stage shouldBe TurnStage.ExecutingActions

  it should "trigger start events" in:
    var eventTriggered = false
    controller.battle.eventManager.watch(TurnStageEvents.Started) { turn =>
      eventTriggered = true
    }

    controller.start()

    eventTriggered shouldBe true
    controller.battle.turn.get.state.stage shouldBe TurnStage.Started

  it should "trigger action received events" in:
    controller.start()
    var eventTriggered = false
    controller.battle.eventManager.watch(TurnStageEvents.ActionsReceived) { turn =>
      eventTriggered = true
    }

    val action1 = SwitchAction()
    val action2 = SwitchAction()

    controller.update()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)

    controller.battle.turn.get.state.stage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()

    eventTriggered shouldBe true
    controller.battle.turn.get.state.stage shouldBe TurnStage.ExecutingActions

  it should "reset turn after the final stage" in:
    controller.start()
    controller.battle.turn.get.state.stage shouldBe TurnStage.Started

    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.WaitingForActions

    val action1 = SwitchAction()
    val action2 = SwitchAction()
    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)
    controller.battle.turn.get.state.stage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.ExecutingActions

    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.Ended

    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.Started