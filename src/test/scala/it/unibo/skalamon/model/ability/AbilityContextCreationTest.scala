package it.unibo.skalamon.model.ability

import it.unibo.skalamon.model.ability.createContext
import it.unibo.skalamon.model.behavior.BehaviorTestUtils.getPlainBehaviors
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the creation of an [[AbilityContext]] from an [[Ability]].
  */
class AbilityContextCreationTest extends AnyFlatSpec with should.Matchers:
  private val target = PokemonTestUtils.simplePokemon1
  private val source = PokemonTestUtils.simplePokemon2

  private object TestEventType extends EventType[Int]

  "Ability with one hook" should "create a context with that behavior" in:
    val behavior = SingleHitBehavior(10)
    val hook = AbilityHook(TestEventType, (_, _, _) => behavior)
    val ability = Ability(
      "TestAbility",
      hooks = hook :: Nil
    )
    val context = ability.createContext(_ => hook.behavior(source, target, 0), target, source)

    context.origin shouldEqual ability
    context.target shouldEqual target
    context.source shouldEqual source
    getPlainBehaviors(context) shouldEqual List(SingleHitBehavior(10))

  "Ability with one hook and multiple behaviors" should "create a context with those behaviors" in:
    val behavior = SingleHitBehavior(10)
    val hook = AbilityHook(
      TestEventType,
      (_, _, _) => BehaviorGroup(behavior, behavior, behavior)
    )
    val ability = Ability(
      "TestAbility",
      hooks = hook :: Nil
    )
    val context = ability.createContext(_ => hook.behavior(source, target, 0), target, source)

    getPlainBehaviors(context) shouldEqual List(behavior, behavior, behavior)
