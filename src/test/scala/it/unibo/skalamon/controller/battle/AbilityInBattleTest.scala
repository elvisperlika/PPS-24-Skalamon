package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.SwitchAction
import it.unibo.skalamon.model.battle.{Battle, BattleState, Trainer}
import it.unibo.skalamon.model.behavior.kind.StatChangeBehavior
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.event.BehaviorEvent
import it.unibo.skalamon.model.field.weather.Rain
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.Stat.{Attack, Speed}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the execution of abilities in battle, from the ability factory.
  */
class AbilityInBattleTest extends AnyFlatSpec with should.Matchers:
  private val neutral = rattata

  extension (pokemon: Pokemon)
    private def b = pokemon.battling(Male)

  extension (battle: Battle)
    private def state: BattleState = battle.currentTurn.get.state.snapshot

  extension (state: BattleState)
    private def inField: (BattlePokemon, BattlePokemon) =
      (state.trainers.head.inField.get, state.trainers.tail.head.inField.get)

  private def newBattle(teamAlice: Pokemon*)(teamBob: Pokemon*): (Battle, BattleController, Trainer, Trainer) =
    val alice = Trainer("Alice", teamAlice.map(_.b).toList)
    val bob = Trainer("Bob", teamBob.map(_.b).toList)
    val battle = Battle(alice :: bob :: Nil)
    val controller = BattleController(battle)
    controller.start()
    (battle, controller, alice, bob)

  "Speed Boost" should "increase source's speed on turn start" in:
    val (battle, controller, _, _) = newBattle(yanmega)(neutral)
    controller.update()
    battle.state.inField._1.statChanges(Speed) shouldBe 1

  "Drizzle" should "set rain" in:
    val (battle, controller, a, b) = newBattle(neutral, pelipper)(neutral)
    controller.update()
    battle.state.field.weather shouldBe None

    controller.registerAction(a, SwitchAction(a.team.last))
    controller.registerAction(b, SwitchAction(b.team.last))

    battle.state.field.weather shouldBe Some(Rain(0))

  "Swift Swim" should "increase source's attack in rain" in:
    val (battle, controller, a, b) = newBattle(neutral, pelipper)(squirtle)
    battle.eventManager.watch(BehaviorEvent[StatChangeBehavior]())(println(_))
    controller.update()
    battle.state.inField._2.statChanges.getOrElse(Attack, 0) shouldBe 0

    controller.registerAction(a, SwitchAction(a.team.last))
    controller.registerAction(b, SwitchAction(b.team.last))
    controller.update()

    battle.state.field.weather shouldBe Some(Rain(0))

    battle.state.inField._2.statChanges(Attack) shouldBe 1