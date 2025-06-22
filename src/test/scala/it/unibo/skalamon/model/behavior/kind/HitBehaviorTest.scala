package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.*
import it.unibo.skalamon.model.behavior.BehaviorTestUtils.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[HitBehavior]] and its implementations.
  */
class HitBehaviorTest extends AnyFlatSpec with should.Matchers {

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
}
