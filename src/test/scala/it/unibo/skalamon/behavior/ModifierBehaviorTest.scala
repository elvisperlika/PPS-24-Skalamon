package it.unibo.skalamon.behavior

import it.unibo.skalamon.behavior.BehaviorTestUtils.*
import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.data.percent
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[HitBehavior]] and its implementations.
  */
class ModifierBehaviorTest extends AnyFlatSpec with should.Matchers {
  private val HIT_POWER = 30

  "Behavior with self-target" should "target the move's source" in:
    val behavior = new SimpleSingleHitBehavior(HIT_POWER)
      with TargetModifier(TargetModifier.Type.Self)
    val result = behavior.apply(context)
    result.behaviors shouldEqual List(
      (
        SingleHitBehavior(30),
        BehaviorModifiers(target = Some(TargetModifier.Type.Self))
      )
    )

  "Behavior with 100% chance" should "never fail" in:
    val behavior = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(100.percent)
    val result = behavior(context)
    getPlainBehaviors(result) shouldEqual List(behavior)

  "Behavior with 0% chance" should "always fail" in:
    val behavior = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(0.percent)
    val result = behavior(context)
    getPlainBehaviors(result) shouldEqual List()

  "Behavior with 50% chance" should "generate randomly" in:
    val generator = alternatingNumberGenerator()
    val behavior1 = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(50.percent, generator = generator)
    val behavior2 = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(50.percent, generator = generator)

    getPlainBehaviors(behavior1(context)) shouldEqual List()
    getPlainBehaviors(behavior2(context)) shouldEqual List(behavior1)
}
