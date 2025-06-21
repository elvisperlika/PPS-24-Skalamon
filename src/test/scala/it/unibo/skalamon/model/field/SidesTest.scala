package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.fieldside.kind.{Spikes, StealthRock}
import it.unibo.skalamon.model.field.fieldside.{FieldSide, SideCondition, add}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SidesTest extends AnyFlatSpec with should.Matchers:

  "Side field" should "add condition" in:
    var side: FieldSide = FieldSide()
    object SimpleSideCondition extends SideCondition
    side = side.add(SimpleSideCondition)
    side shouldEqual FieldSide(SimpleSideCondition :: Nil)

  it should "not add unique condition if one exist in field" in:
    val stealthRock: SideCondition = StealthRock(1)
    var side: FieldSide = FieldSide()
    side = side.add(stealthRock)
    side shouldEqual FieldSide(stealthRock :: Nil)
    val stealthRock2: SideCondition = StealthRock(2)
    side = side.add(stealthRock2)
    side shouldEqual FieldSide(stealthRock :: Nil)

  it should "add multiple condition until limit" in:
    val spikes: SideCondition = Spikes(creationTurn = 1)
    var sideWithSpike: FieldSide = FieldSide(spikes :: Nil)
    val spikes2: SideCondition = Spikes(creationTurn = 2)
    sideWithSpike = sideWithSpike.add(spikes2)
    sideWithSpike shouldEqual FieldSide(spikes2 :: spikes :: Nil)
    val spikes3: SideCondition = Spikes(creationTurn = 3)
    sideWithSpike = sideWithSpike.add(spikes3)
    sideWithSpike shouldEqual FieldSide(spikes3 :: spikes2 :: spikes :: Nil)
    val spikes4: SideCondition = Spikes(creationTurn = 4)
    sideWithSpike = sideWithSpike.add(spikes4)
    sideWithSpike shouldEqual FieldSide(spikes3 :: spikes2 :: spikes :: Nil)
