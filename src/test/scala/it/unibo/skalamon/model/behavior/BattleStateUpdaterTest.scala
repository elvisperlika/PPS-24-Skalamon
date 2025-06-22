package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.TargetModifier
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** */
class BattleStateUpdaterTest extends AnyFlatSpec with should.Matchers:
  private val alice = PokemonTestUtils.trainerAlice
  private val bob = PokemonTestUtils.trainerBob

  private val source = alice.team.head
  private val target = bob.team.head

  private val state = BattleState(alice :: bob :: Nil)

  private val context = BehaviorTestUtils.context(
    target = target,
    source = source
  )

  private def getSource(state: BattleState): BattlePokemon =
    state.trainers.find(_.name == alice.name).get.team.head

  private def getTarget(state: BattleState): BattlePokemon =
    state.trainers.find(_.name == bob.name).get.team.head

  "HealthBehavior" should "update the target's health" in:
    val damage = 10
    val behavior = DamageBehavior(damage)
    val newState = behavior(context)(state)
    getTarget(newState).currentHP shouldBe target.currentHP - damage
    getSource(newState).currentHP shouldBe source.currentHP

  "HealthBehavior with self-target" should "update self's health" in:
    val damage = 10
    val behavior = new DamageBehavior(damage) with TargetModifier(TargetModifier.Type.Self)
    val newState = behavior(context)(state)
    getTarget(newState).currentHP shouldBe target.currentHP
    getSource(newState).currentHP shouldBe source.currentHP - damage
