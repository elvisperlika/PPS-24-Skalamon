package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.battle.{Battle, TurnStage}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[BattleController]].
  */
class BattleControllerTest extends AnyFlatSpec with should.Matchers:
  private val alice = trainerAlice
  private val bob = trainerBob

  private val battle = Battle(List(alice, bob))

  private val controller = BattleController(battle)

  "Battle controller" should "begin in started stage" in:
    controller.battle.turn.state.stage shouldBe TurnStage.Started

  it should "advance to waiting for actions stage" in:
    controller.update()
    controller.battle.turn.state.stage shouldBe TurnStage.WaitingForActions

  it should "register actions from trainers" in:
    val action1 = SwitchAction()
    val action2 = SwitchAction()

    controller.registerAction(alice, action1)
    controller.registerAction(bob, action2)

    controller.battle.turn.state.stage shouldBe TurnStage.ActionsReceived(
      ActionBuffer(2).register(alice, action1).register(bob, action2)
    )

    controller.update()

    controller.battle.turn.state.stage shouldBe TurnStage.ExecutingActions

// seems good. todo refactor all, document all, commit
