package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.move.Move.*
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Pokemon}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the execution of moves in battle, from the move factory.
  */
class MoveInBattleTest extends AnyFlatSpec with should.Matchers
    with BattleSimulationTest:

  private def registerMoves(
      a: Move,
      b: Move
  )(controller: BattleController): Unit =
    val (trainerA, trainerB) = controller.battle.trainersTuple
    controller.registerAction(
      trainerA,
      MoveAction(trainerA.inField.get.move(a), trainerA, trainerB)
    )
    controller.registerAction(
      trainerB,
      MoveAction(trainerB.inField.get.move(b), trainerB, trainerA)
    )

  extension (pokemon: BattlePokemon)
    def move(move: Move): BattleMove =
      pokemon.moves.find(_.move.name == move.name).get

  "Tackle" should "just cause damage" in:
    val (battle, controller, _, _) = newBattle(pikachu)(rattata)
    controller.update()
    registerMoves(tackle, tackle)(controller)

    battle.state.inField._1.currentHP shouldBe <(pikachu.hp)
    battle.state.inField._2.currentHP shouldBe <(rattata.hp)

  "Dragon Rage" should "cause fixed damage" in:
    val (battle, controller, _, _) = newBattle(dragonite)(pelipper)
    controller.update()
    registerMoves(dragonRage, swift)(controller)

    battle.state.inField._1.currentHP shouldBe <(dragonite.hp)
    battle.state.inField._2.currentHP shouldBe pelipper.hp - 40

  "Roost" should "heal the user" in:
    val (battle, controller, _, _) = newBattle(dragonite)(pelipper)
    controller.update()
    registerMoves(dragonRage, roost)(controller)

    battle.state.inField._1.currentHP shouldBe dragonite.hp
    battle.state.inField._2.currentHP shouldBe pelipper.hp

  "Sword Dance" should "increase the user's attack" in:
    val (battle, controller, _, _) = newBattle(lucario)(lucario)
    controller.update()
    registerMoves(swordDance, quickAttack)(controller)

    val deltaHp = lucario.hp - battle.state.inField._1.currentHP
    battle.state.inField._1.currentHP shouldBe <(lucario.hp)
    battle.state.inField._2.currentHP shouldBe lucario.hp

    advanceToNextRegistration(controller)
    registerMoves(quickAttack, swordDance)(controller)

    battle.state.inField._1.currentHP shouldBe lucario.hp - deltaHp
    battle.state.inField._2.currentHP shouldBe lucario.hp - deltaHp * 2