package it.unibo.skalamon.behavior

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.pokemon.MutablePokemon
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[HitBehavior]] and its implementations.
  */
class HitBehaviorTest extends AnyFlatSpec with should.Matchers {
  private val context = MoveContext(
    move = MutableMove(
      Move("TestMove"),
      pp = 10
    ),
    target = MutablePokemon(hp = 100),
    source = MutablePokemon(hp = 100)
  )

  private def getPlainBehaviors(context: MoveContext): List[Behavior] =
    context.behaviors.map(_._1)

  "SingleHitBehavior" should "apply a single hit" in:
    val behavior = SingleHitBehavior(30)
    val result = behavior(context)
    getPlainBehaviors(result) shouldEqual List(SingleHitBehavior(30))

  "MultiHitBehavior" should "apply multiple hits with the same power" in:
    val behavior = MultiHitBehavior(hits = 3, power = 20)
    val result = behavior(context)
    getPlainBehaviors(result) shouldEqual List(
      SingleHitBehavior(20),
      SingleHitBehavior(20),
      SingleHitBehavior(20)
    )

  "MultiHitBehavior" should "apply multiple hits with the different power" in:
    val behavior =
      MultiHitBehavior(hits = 3, power = hitIndex => (hitIndex + 1) * 10)
    val result = behavior(context)
    getPlainBehaviors(result) shouldEqual List(
      SingleHitBehavior(10),
      SingleHitBehavior(20),
      SingleHitBehavior(30)
    )

  "SingleHitBehavior with self-target" should "target the source" in:
    val behavior = new SimpleSingleHitBehavior(30)
      with TargetModifier(TargetModifier.Type.Self)
    val result = behavior.apply(context)
    result.behaviors shouldEqual List(
      (
        SingleHitBehavior(30),
        BehaviorModifiers(target = Some(TargetModifier.Type.Self))
      )
    )

  "SingleHitBehavior with 100% chance" should "never fail" in:
    val behavior = new SimpleSingleHitBehavior(30)
      with ProbabilityModifier(100.percent)
    val result = behavior.apply(context)
    getPlainBehaviors(result) shouldEqual List(behavior)

  "SingleHitBehavior with 0% chance" should "always fail" in:
    val behavior = new SimpleSingleHitBehavior(30)
      with ProbabilityModifier(0.percent)
    val result = behavior.apply(context)
    getPlainBehaviors(result) shouldEqual List()
}
