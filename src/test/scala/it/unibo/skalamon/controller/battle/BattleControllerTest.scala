package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.{Battle, TurnStage}
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

  "Battle controller" should "begin in started stage" in:
    controller.battle.turn.get.state.stage shouldBe TurnStage.Started

  it should "advance to waiting for actions stage" in:
    controller.update()
    controller.battle.turn.get.state.stage shouldBe TurnStage.WaitingForActions

  it should "not allow action registration in started stage" in:
    val action = SwitchAction()
    an[IllegalStateException] should be thrownBy controller.registerAction(alice, action)

  it should "register actions from trainers" in:
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