package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.SwitchAction
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.behavior.kind.StatChangeBehavior
import it.unibo.skalamon.model.event.BehaviorEvent
import it.unibo.skalamon.model.field.weather.{Rain, Sandstorm}
import it.unibo.skalamon.model.pokemon.Pokemon
import it.unibo.skalamon.model.pokemon.Pokemon.*
import it.unibo.skalamon.model.pokemon.Stat.{Attack, Speed}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the execution of abilities in battle, from the ability factory.
  */
class AbilityInBattleTest extends AnyFlatSpec with should.Matchers with BattleSimulationTest:
  
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

  "Sand Stream" should "set sandstorm" in:
    val (battle, controller, a, b) = newBattle(neutral, tyranitar)(neutral)
    controller.update()
    battle.state.field.weather shouldBe None

    controller.registerAction(a, SwitchAction(a.team.last))
    controller.registerAction(b, SwitchAction(b.team.last))

    battle.state.field.weather shouldBe Some(Sandstorm(0))

    controller.update() // exe action
    controller.update() // ended
    controller.update()
    controller.update() // started

    val (notHurt, hurt) = battle.state.inField
    notHurt.currentHP shouldBe notHurt.base.hp
    hurt.currentHP shouldBe <(hurt.base.hp)

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