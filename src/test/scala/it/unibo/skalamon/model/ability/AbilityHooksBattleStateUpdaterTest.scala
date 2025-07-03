package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.battle.Battle
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.event.TurnStageEvents
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
      hooks = AbilityHook(TurnStageEvents.Started, (_, _, _) => behavior) :: Nil
    )

    ability.hookAll(battle)(
      Some(target),
      Some(source)
    )

    battle.start()
    getTarget(
      battle.currentTurn.get.state.snapshot
    ).currentHP shouldEqual target.currentHP - damage

  "Ability with hook" should "use the event data to affect behavior" in:
    val damage = 10
    val ability = Ability(
      "TestAbility",
      hooks = AbilityHook(
        TurnStageEvents.Started,
        (_, _, turn) => DamageBehavior(turn.state.stage.ordinal + damage)
      ) :: Nil
    )

    ability.hookAll(battle)(
      Some(target),
      Some(source)
    )

    battle.start()
    getTarget(
      battle.currentTurn.get.state.snapshot
    ).currentHP shouldEqual target.currentHP - damage
    
  "Ability with hook" should "use the Pokemon data to affect behavior" in:
    val ability = Ability(
      "TestAbility",
      hooks = AbilityHook(
        TurnStageEvents.Started,
        (source, target, _) => DamageBehavior(source.currentHP - target.currentHP)
      ) :: Nil
    )

    ability.hookAll(battle)(
      Some(target),
      Some(source)
    )

    battle.start()
    getTarget(
      battle.currentTurn.get.state.snapshot
    ).currentHP shouldEqual target.currentHP - (source.currentHP - target.currentHP)

  "Ability" should "not apply if the source is missing" in:
    val damage = 10
    val behavior = DamageBehavior(damage)
    val ability = Ability(
      "TestAbility",
      hooks = AbilityHook(TurnStageEvents.Started, (_, _, _) => behavior) :: Nil
    )

    ability.hookAll(battle)(
      Some(target),
      None
    )

    battle.start()
    getTarget(
      battle.currentTurn.get.state.snapshot
    ).currentHP shouldEqual target.currentHP
