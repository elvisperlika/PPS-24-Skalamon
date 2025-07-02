package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.field.fieldside.{FieldSide, SideCondition}
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.field.terrain.Mud
import it.unibo.skalamon.model.field.weather.Sunny
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FieldBuilderTest extends AnyFlatSpec with should.Matchers:

  val bob: Trainer = Trainer("Bob", Nil, None)
  val alice: Trainer = Trainer("Alice", Nil, None)

  "Field Builder" should "let create empty field" in:
    val empty: Field = field(bob :: Nil)()
    empty shouldBe Field(Map(bob -> FieldSide()), None, None, None)

  it should "let create field with Terrain" in:
    val mudField: Field = field(bob :: Nil) { b =>
      b.setTerrain(Mud(1))
    }
    mudField shouldBe Field(
      Map(bob -> FieldSide()),
      Some(Mud(1)),
      None,
      None
    )

  it should "let create field with Room" in:
    val darkField: Field = field(bob :: Nil) { b =>
      b.setRoom(TrickRoom(1))
    }
    darkField shouldBe Field(
      Map(bob -> FieldSide()),
      None,
      Some(TrickRoom(1)),
      None
    )

  it should "let create field with Weather" in:
    val foggyField: Field = field(bob :: alice :: Nil) { b =>
      b.setWeather(Sunny(1))
    }
    foggyField shouldBe Field(
      Map(bob -> FieldSide(), alice -> FieldSide()),
      None,
      None,
      Some(Sunny(1))
    )

  it should "let create combinations" in:
    val foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain(Mud(2))
      b.setWeather(Sunny(2))
      b.setRoom(TrickRoom(1))
    }
    foggyMudAndDark shouldBe Field(
      Map(alice -> FieldSide(), bob -> FieldSide()),
      Some(Mud(2)),
      Some(TrickRoom(1)),
      Some(Sunny(2))
    )

  it should "let change side" in:
    var foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain(Mud(2))
      b.setWeather(Sunny(3))
      b.setRoom(TrickRoom(1))
    }
    object SimpleCondition extends SideCondition
    object SimpleFieldSide extends FieldSide(SimpleCondition :: Nil)
    foggyMudAndDark = foggyMudAndDark.changeSide(alice, SimpleFieldSide)
    foggyMudAndDark shouldBe Field(
      Map(alice -> SimpleFieldSide, bob -> FieldSide()),
      Some(Mud(2)),
      Some(TrickRoom(1)),
      Some(Sunny(3))
    )
    foggyMudAndDark.sides(alice) shouldEqual SimpleFieldSide
