package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.behavior.kind.{
  HealBehavior,
  SimpleSingleHitBehavior,
  SingleHitBehavior
}
import it.unibo.skalamon.model.behavior.modifier.{BehaviorGroup, TargetModifier}
import it.unibo.skalamon.model.move.MoveModel.Category
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Normal}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building moves via DSL. */
class MoveDslTest extends AnyFlatSpec with should.Matchers:

  "Move DSL" should "create empty moves" in:
    val tackle = move("Tackle", Normal, Category.Physical)(identity)

    tackle.name shouldBe "Tackle"
    tackle.priority shouldBe 0
    tackle.moveType shouldBe Normal
    tackle.category shouldBe Category.Physical
    tackle.success shouldBe EmptyBehavior
    tackle.fail shouldBe EmptyBehavior

  it should "allow setting priority" in:
    val quickAttack = move("Quick Attack", Normal, Category.Physical):
      _ priority 1

    quickAttack.priority shouldBe 1

  it should "allow setting behaviors" in:
    val thunderbolt = move("Thunderbolt", Electric, Category.Special):
      _ onSuccess SingleHitBehavior(10) onFail HealBehavior(5)

    thunderbolt.success shouldBe SingleHitBehavior(10)
    thunderbolt.fail shouldBe HealBehavior(5)

  it should "allow multiple behaviors" in:
    val thunderbolt = move("Thunderbolt", Electric, Category.Special):
      _ onSuccess groupOf(
        SingleHitBehavior(10),
        HealBehavior(5)
      )

    thunderbolt.success shouldBe BehaviorGroup(SingleHitBehavior(10), HealBehavior(5))
    thunderbolt.fail shouldBe EmptyBehavior

  it should "allow modified behaviors" in:
    val thunderbolt = move("Thunderbolt", Electric, Category.Special):
      _ onSuccess new SimpleSingleHitBehavior(10) with TargetModifier(TargetModifier.Type.Self)

    thunderbolt.success shouldBe new SimpleSingleHitBehavior(10) with TargetModifier(TargetModifier.Type.Self)