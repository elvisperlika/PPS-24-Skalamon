package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.{BehaviorGroup, TargetModifier}
import it.unibo.skalamon.model.event.{BattleStateEvents, EventManager}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import it.unibo.skalamon.model.status.{Burn, Confusion, Paralyze, Yawn}
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

  private val damage = 10

  "HealthBehavior" should "update the target's health" in:
    val behavior = DamageBehavior(damage)
    val newState = behavior(context)(state)
    getTarget(newState).currentHP shouldBe target.currentHP - damage
    getSource(newState).currentHP shouldBe source.currentHP

  "HealthBehavior with self-target" should "update self's health" in:
    val behavior =
      new DamageBehavior(damage) with TargetModifier(TargetModifier.Type.Self)
    val newState = behavior(context)(state)
    getTarget(newState).currentHP shouldBe target.currentHP
    getSource(newState).currentHP shouldBe source.currentHP - damage

  "StatusBehavior" should "set volatile status" in:
    val status = Confusion
    val behavior = StatusBehavior(status, currentTurnIndex = 1)
    val newState = behavior(context)(state)
    getTarget(newState).volatileStatus.map(_.status) shouldBe Set(status)

  it should "stack volatile status" in:
    val status1 = Confusion
    val status2 = Yawn
    val newState1 =
      StatusBehavior(status1, currentTurnIndex = 1)(context)(state)
    val newState2 =
      StatusBehavior(status2, currentTurnIndex = 1)(context)(newState1)
    getTarget(newState2).volatileStatus.map(_.status) shouldBe Set(
      status1,
      status2
    )

  it should "set non-volatile status" in:
    val status = Burn
    val newState = StatusBehavior(status, currentTurnIndex = 1)(context)(state)
    getTarget(newState).nonVolatileStatus.map(_.status) shouldBe Some(status)

  it should "not overwrite existing non-volatile status" in:
    val status1 = Burn
    val status2 = Paralyze
    val newState1 =
      StatusBehavior(status1, currentTurnIndex = 1)(context)(state)
    val newState2 =
      StatusBehavior(status2, currentTurnIndex = 1)(context)(newState1)
    getTarget(newState2).nonVolatileStatus.map(_.status) shouldBe Some(status1)

  it should "trigger state changed events" in:
    given manager: EventManager = EventManager()
    val behavior = DamageBehavior(damage)

    var notified = false
    manager.watch(BattleStateEvents.Changed) { case (oldState, newState) =>
      oldState shouldEqual state
      notified = true
    }

    behavior(context)(state)

    notified shouldBe true

  it should "trigger multiple state changed events for each behavior" in:
    given manager: EventManager = EventManager()

    val behavior = BehaviorGroup(DamageBehavior(damage), StatusBehavior(Confusion, 1))

    var currentState = state
    var count = 0
    manager.watch(BattleStateEvents.Changed) { case (oldState, newState) =>
      oldState shouldEqual currentState
      currentState = newState
      count += 1
    }

    behavior(context)(state)

    count shouldBe behavior.behaviors.size