package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.model.field.weather.Rain
import it.unibo.skalamon.model.move.Move.*
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.Stat.{Attack, Speed}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Pokemon}
import it.unibo.skalamon.model.status.nonVolatileStatus.{Burn, Paralyze}
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

    battle.state.inField._1.statChanges(Attack) shouldBe 2

  "Dragon Dance" should "increase the user's attack and speed" in:
    val (battle, controller, _, _) = newBattle(dragonite)(rattata)
    controller.update()
    registerMoves(dragonDance, tackle)(controller)

    battle.state.inField._1.statChanges(Attack) shouldBe 1
    battle.state.inField._1.statChanges(Speed) shouldBe 1

  "Growl" should "cancel out Dragon Dance's attack boost" in:
    val (battle, controller, _, _) = newBattle(dragonite)(rattata)
    controller.update()
    registerMoves(dragonDance, growl)(controller)

    battle.state.inField._1.statChanges(Attack) shouldBe 0

  "Thunderwave" should "paralyze the target" in:
    val (battle, controller, _, _) = newBattle(pikachu)(rattata)
    controller.update()
    registerMoves(thunderWave, tackle)(controller)

    battle.state.inField._2.nonVolatileStatus.map(_.status) shouldBe Some(
      Paralyze()
    )

  "Will-O-Wisp" should "burn the target" in:
    val (battle, controller, _, _) = newBattle(charmander)(rattata)
    controller.update()
    registerMoves(willOWisp, tackle)(controller)

    battle.state.inField._2.nonVolatileStatus.map(_.status) shouldBe Some(Burn())

  "Earthquake" should "not affect flying Pokémon" in:
    val (battle, controller, _, _) = newBattle(gyarados)(dragonite)
    controller.update()
    registerMoves(earthquake, dragonClaw)(controller)

    battle.state.inField._1.currentHP shouldBe <(gyarados.hp)
    battle.state.inField._2.currentHP shouldBe dragonite.hp

  "Grass Knot" should "deal damage based on the target's weight" in:
    val (battle, controller, _, _) =
      newBattle(bulbasaur)(bulbasaur.copy(weightKg = bulbasaur.weightKg * 2))
    controller.update()
    registerMoves(grassKnot, grassKnot)(controller)

    val lightweightDeltaHp = bulbasaur.hp - battle.state.inField._1.currentHP
    val heavyweightDeltaHp = gyarados.hp - battle.state.inField._2.currentHP

    lightweightDeltaHp shouldBe <(heavyweightDeltaHp)

  "Super Fang" should "halve the target's HP" in:
    val (battle, controller, _, _) = newBattle(rattata)(rattata)
    controller.update()
    registerMoves(superFang, tackle)(controller)

    battle.state.inField._2.currentHP shouldBe rattata.hp / 2

  "Bullet Seed" should "hit multiple times" in:
    val (battle, controller, _, _) = newBattle(bulbasaur)(rattata)

    var counter = 0
    battle.eventManager.watch(BattleStateEvents.Changed)(_ => counter += 1)

    controller.update()
    registerMoves(bulletSeed, growl)(controller)

    counter should be > 1

  "Rain Dance" should "cause rain" in:
    val (battle, controller, _, _) = newBattle(pelipper)(pelipper)
    controller.update()
    registerMoves(rainDance, aquaJet)(controller)

    battle.state.field.weather shouldBe Some(Rain(0))

  "Rain" should "boost Water moves" in:
    val (battle, controller, _, _) = newBattle(pelipper)(pelipper)
    controller.update()
    registerMoves(aquaJet, aquaJet)(controller)

    val deltaHpBeforeRain = pelipper.hp - battle.state.inField._1.currentHP

    advanceToNextRegistration(controller)
    registerMoves(rainDance, rainDance)(controller)
    advanceToNextRegistration(controller)

    registerMoves(aquaJet, aquaJet)(controller)
    val deltaHpAfterRain =
      pelipper.hp - deltaHpBeforeRain - battle.state.inField._1.currentHP

    deltaHpAfterRain should be > deltaHpBeforeRain
