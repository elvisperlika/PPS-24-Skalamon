package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.Battle
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.event.{EventType, TurnStageEvents}
import it.unibo.skalamon.utils.MockTrainers
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the execution of an [[Ability]] through hooks.
  * @see
  *   [[BattleStateUpdaterEventManagerTest]]
  */
class AbilityHooksBattleStateUpdaterTest extends AnyFlatSpec
    with should.Matchers with BeforeAndAfterEach with MockTrainers:
  private var battle = Battle(List(alice, bob))

  override def beforeEach(): Unit =
    battle = Battle(List(alice, bob))

  "Ability with turn started hook" should "apply that hook" in:
    val damage = 10
    val behavior = DamageBehavior(damage)
    val ability = Ability(
      "TestAbility",
      hooks = Map(TurnStageEvents.Started -> behavior)
    )

    ability.hookAll(
      battle.eventManager,
      target,
      source,
    )

    battle.start()
    getTarget(battle.currentTurn.get.state.snapshot).currentHP shouldEqual target.currentHP - damage
