package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.BehaviorTestUtils.*
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.data.{RandomGenerator, percent}
import it.unibo.skalamon.utils.Seeded
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[HitBehavior]] and its implementations.
  */
class ModifierBehaviorTest extends AnyFlatSpec with should.Matchers with Seeded:
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
    given RandomGenerator = alternatingNumberGenerator()
    val behavior1 = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(50.percent)
    val behavior2 = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(50.percent)

    getPlainBehaviors(behavior1(context)) shouldEqual List()
    getPlainBehaviors(behavior2(context)) shouldEqual List(behavior1)

  "HitBehavior with ranging damage" should "generate randomly" in:
    val MAX_HIT_POWER = 50
    given RandomGenerator = alternatingNumberGenerator()
    val behavior =
      RandomModifier(HIT_POWER, MAX_HIT_POWER)(SimpleSingleHitBehavior(_))

    getPlainBehaviors(behavior(context)) shouldEqual List(
      SingleHitBehavior(MAX_HIT_POWER)
    )
    getPlainBehaviors(behavior(context)) shouldEqual List(
      SingleHitBehavior(HIT_POWER)
    )

  "Grouped behaviors" should "apply all behaviors in the group" in:
    val behavior1 = new SimpleSingleHitBehavior(HIT_POWER)
      with ProbabilityModifier(100.percent)
    val behavior2 = new SimpleSingleHitBehavior(HIT_POWER)
      with TargetModifier(TargetModifier.Type.Self)

    val group = BehaviorGroup(behavior1, behavior2, behavior1)
    val result = group(context)

    result.behaviors shouldEqual List(
      (SingleHitBehavior(HIT_POWER), BehaviorModifiers()),
      (
        SingleHitBehavior(HIT_POWER),
        BehaviorModifiers(target = Some(TargetModifier.Type.Self))
      ),
      (SingleHitBehavior(HIT_POWER), BehaviorModifiers())
    )

  "Grouped behaviors with modifier" should "apply the behavior to children" in:
    val behavior = SimpleSingleHitBehavior(HIT_POWER)

    val group = new BehaviorGroup(behavior, behavior) with TargetModifier(TargetModifier.Type.Self)
    val result = group(context)

    result.behaviors shouldEqual List(
      (SingleHitBehavior(HIT_POWER), BehaviorModifiers(target = Some(TargetModifier.Type.Self))),
      (SingleHitBehavior(HIT_POWER), BehaviorModifiers(target = Some(TargetModifier.Type.Self)))
    )
