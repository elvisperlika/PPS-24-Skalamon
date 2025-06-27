package it.unibo.skalamon.model.behavior.kind

import it.unibo.skalamon.model.behavior.BehaviorTestUtils.*
import it.unibo.skalamon.model.pokemon.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Test for [[StatChangeBehavior]] and related stat mechanics.
  */
class StatChangeBehaviorTest extends AnyFlatSpec with Matchers:

  "StatStage.clamp" should "cap values within -6 and +6" in:
    StatStage.clamp(10) shouldBe 6
    StatStage.clamp(-10) shouldBe -6
    StatStage.clamp(3) shouldBe 3
    StatStage.clamp(0) shouldBe 0

  "StatStage.multiplier" should "return correct multipliers for positive stages" in:
    StatStage.multiplier(0) shouldBe 1.0 +- 0.0001
    StatStage.multiplier(1) shouldBe 1.5 +- 0.0001
    StatStage.multiplier(2) shouldBe 2.0 +- 0.0001
    StatStage.multiplier(3) shouldBe 2.5 +- 0.0001
    StatStage.multiplier(4) shouldBe 3.0 +- 0.0001
    StatStage.multiplier(5) shouldBe 3.5 +- 0.0001
    StatStage.multiplier(6) shouldBe 4.0 +- 0.0001

    StatStage.multiplier(-1) shouldBe (2.0/3.0) +- 0.0001
    StatStage.multiplier(-2) shouldBe 0.5 +- 0.0001
    StatStage.multiplier(-3) shouldBe (2.0/5.0) +- 0.0001
    StatStage.multiplier(-4) shouldBe (1.0/3.0) +- 0.0001
    StatStage.multiplier(-5) shouldBe (2.0/7.0) +- 0.0001
    StatStage.multiplier(-6) shouldBe (1.0/4.0) +- 0.0001

  it should "return correct multipliers for negative stages" in:
    StatStage.multiplier(-1) shouldBe (2.0 / 3.0) +- 0.0001
    StatStage.multiplier(-2) shouldBe (2.0 / 4.0) +- 0.0001
    StatStage.multiplier(-6) shouldBe (2.0 / 8.0) +- 0.0001

  "StatChangeBehavior" should "bring stat change" in:
    val stage = 2
    val behavior = StatChangeBehavior(Stat.Attack + stage)
    val result = behavior.apply(context)
    getPlainBehaviors(result) shouldEqual List(
      StatChangeBehavior(StatChange(Stat.Attack, stage))
    )
