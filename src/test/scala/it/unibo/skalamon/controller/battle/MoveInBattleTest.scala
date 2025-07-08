package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.event.BattleStateEvents
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.field.weather.{Rain, Sunny}
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.move.Move.*
import it.unibo.skalamon.model.pokemon.Pokemon
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.Stat.{Attack, Speed}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Pokemon}
import it.unibo.skalamon.model.status.nonVolatileStatus.{Burn, Paralyze, Sleep}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the execution of moves in battle, from the move factory.
  */
class MoveInBattleTest extends AnyFlatSpec with should.Matchers
    with BattleSimulationTest:

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

  "Sunny Day" should "cause sun" in:
    val (battle, controller, _, _) = newBattle(charmander)(charmander)
    controller.update()
    registerMoves(sunnyDay, sunnyDay)(controller)

    battle.state.field.weather shouldBe Some(Sunny(0))

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


  "Rest" should "put the user to sleep and heal it" in:
    val (battle, controller, _, _) = newBattle(snorlax)(rattata)
    controller.update()
    registerMoves(rest, quickAttack)(controller)

    battle.state.inField._1.currentHP shouldBe snorlax.hp
    battle.state.inField._1.nonVolatileStatus.map(_.status) shouldBe Some(Sleep)

  "Trick Room" should "apply that room" in:
    val (battle, controller, _, _) = newBattle(malamar)(rattata)
    controller.update()
    registerMoves(trickRoom, growl)(controller)

    battle.state.field.room shouldBe Some(TrickRoom(0))

  "PP" should "decrement after move execution" in:
    val (battle, controller, _, _) = newBattle(pelipper)(pelipper)
    controller.update()

    while (battle.state.inField._1.move(rainDance).pp > 0) {
      registerMoves(rainDance, rainDance)(controller)
      advanceToNextRegistration(controller)
    }

  "KO-ing" should "make the trainer switch" in:
    val (battle, controller, a, b) = newBattle(rattata)(rattata, gyarados)
    controller.update()

    while (battle.state.inField._2.base.name == rattata.name) {
      registerMoves(tackle, growl)(controller)
      advanceToNextRegistration(controller)
    }

    battle.state.inField._2.base.name shouldBe gyarados.name

  "Game over" should "trigger when there is only on trainer with at least a Pokémon alive" in:
    val (battle, controller, a, b) = newBattle(rattata)(rattata)
    controller.update()

    var notified = false
    battle.eventManager.watch(BattleStateEvents.Finished) { winner =>
      winner.map(_.id) shouldBe Some(a.id)
      notified = true
    }

    while (battle.gameState == GameState.InProgress) {
      registerMoves(tackle, growl)(controller)
      advanceToNextRegistration(controller)
    }

    notified shouldBe true