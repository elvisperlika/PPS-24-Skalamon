package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.move.MoveModel.{Accuracy, Category}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import it.unibo.skalamon.model.status.volatileStatus.{Flinch, Yawn}
import it.unibo.skalamon.model.types.TypesCollection.Ice
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class StatusExecutionTest extends AnyFlatSpec with should.Matchers:

  val trainerKirk: Trainer = PokemonTestUtils.trainerKirk
  val trainerLuca: Trainer = PokemonTestUtils.trainerLuca
  val trainerBob: Trainer = PokemonTestUtils.trainerBob

  val emptyMove: Move = Move(
    name = "Empty Move",
    moveType = Ice,
    category = Category.Status,
    accuracy = Accuracy.Of(100.percent),
    pp = 10,
    success = _ => EmptyBehavior,
    fail = _ => EmptyBehavior
  )

  def emptyMoveAction(trainer: Trainer): MoveAction =
    MoveAction(BattleMove(emptyMove, 1), trainer, trainer)

  def runOneTurn(controller: BattleController): Unit =
    controller.update()
    controller.update()
    controller.battle.trainers.foreach { trainer =>
      controller.registerAction(trainer, emptyMoveAction(trainer))
    }
    controller.update() // end turn
    controller.update() // start next turn

  def currentPokemon(battle: Battle, trainer: Trainer): BattlePokemon =
    battle.currentTurn.get.state.snapshot.trainers
      .find(_.id == trainer.id).get.inField.get

  "NonVolatileStatus" should "apply effects at end of turn (e.g., Burn damage)" in:
    val originalHP = trainerLuca.inField.get.currentHP
    val battle = Battle(List(trainerLuca, trainerKirk))
    val controller = BattleController(battle)
    controller.start()

    runOneTurn(controller)

    val updatedHP = currentPokemon(battle, trainerLuca).currentHP
    withClue("Burn should reduce HP at end of turn:") {
      updatedHP should be < originalHP
    }

  "VolatileStatus" should "apply effects at end of turn (e.g., Flinch, Protect)" in:
    val originalPokemon = trainerKirk.inField.get
    val battle = Battle(List(trainerKirk, trainerLuca))
    val controller = BattleController(battle)
    controller.start()

    runOneTurn(controller)

    val newPokemon = currentPokemon(battle, trainerKirk)

    withClue("Flinch should cause skipTurn:") {
      originalPokemon.skipsCurrentTurn shouldBe false
      newPokemon.skipsCurrentTurn shouldBe true
    }

    withClue("Protect should be applied:") {
      originalPokemon.isProtected shouldBe false
      newPokemon.isProtected shouldBe true
    }

  "VolatileStatus" should "expire after their duration (Flinch and Yawn)" in:
    val battle = Battle(List(trainerKirk, trainerLuca))
    val controller = BattleController(battle)
    controller.start()

    runOneTurn(controller)

    val afterFirstTurn = currentPokemon(battle, trainerKirk)
    afterFirstTurn.volatileStatus.exists(
      _.status.isInstanceOf[Flinch]
    ) shouldBe false
    afterFirstTurn.volatileStatus.exists(
      _.status.isInstanceOf[Yawn]
    ) shouldBe true

    runOneTurn(controller)

    val afterSecondTurn = currentPokemon(battle, trainerKirk)
    afterSecondTurn.volatileStatus.exists(
      _.status.isInstanceOf[Yawn]
    ) shouldBe false

  "Status" should "reset volatile flags at the start of the new turn" in:
    val flaggedPokemon = trainerBob.inField.get.copy(
      isProtected = true,
      skipsCurrentTurn = true
    )
    val modifiedTrainer = trainerBob.copy(_inField = Some(flaggedPokemon))
    val battle = Battle(List(modifiedTrainer, trainerKirk))
    val controller = BattleController(battle)
    controller.start()

    runOneTurn(controller)

    val pokemonAfter = currentPokemon(battle, modifiedTrainer)

    withClue("Flags should reset after turn:") {
      pokemonAfter.isProtected shouldBe false
      pokemonAfter.skipsCurrentTurn shouldBe false
    }
