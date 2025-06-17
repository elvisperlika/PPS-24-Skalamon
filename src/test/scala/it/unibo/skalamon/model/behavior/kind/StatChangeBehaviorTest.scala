package it.unibo.skalamon.model.behavior.kind

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
    StatStage.multiplier(0) shouldBe 1.0
    StatStage.multiplier(1) shouldBe 1.5
    StatStage.multiplier(2) shouldBe 2.0
    StatStage.multiplier(6) shouldBe 4.0

  it should "return correct multipliers for negative stages" in:
    StatStage.multiplier(-1) shouldBe (2.0 / 3.0) +- 0.0001
    StatStage.multiplier(-2) shouldBe (2.0 / 4.0) +- 0.0001
    StatStage.multiplier(-6) shouldBe (2.0 / 8.0) +- 0.0001

  "Stats.applyChange" should "correctly update stage modifiers" in:
    val baseStats = Map(Stat.Attack -> 100)
    val stats = Stats(base = baseStats)

    val updated = stats.applyChange(Stat.Attack + 2)
    updated.stages(Stat.Attack) shouldBe 2

    val clamped = updated.applyChange(Stat.Attack + 10)
    clamped.stages(Stat.Attack) shouldBe 6

    val lowered = clamped.applyChange(Stat.Attack + -20)
    lowered.stages(Stat.Attack) shouldBe -6

  "Stats.effective" should "calculate correct stat values with stage modifiers" in:
    val baseStats = Map(Stat.Attack -> 100)
    val stats = Stats(base = baseStats)

    stats.effective(Stat.Attack) shouldBe 100.0

    val modified = stats.applyChange(Stat.Attack + 1)
    modified.effective(Stat.Attack) shouldBe 150.0

    val decreased = modified.applyChange(Stat.Attack + -2)
    decreased.effective(
      Stat.Attack
    ) shouldBe (100.0 * StatStage.multiplier(-1)) +- 0.001

  "StatChangeBehavior" should "wrap a stat change correctly" in:
    val change = Stat.Attack + 2
    val behavior = StatChangeBehavior(change)

    behavior.change shouldBe StatChange(Stat.Attack, 2)
