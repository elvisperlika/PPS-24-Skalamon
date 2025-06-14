package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.fieldside.FieldSide
import it.unibo.skalamon.model.field.room.Room
import it.unibo.skalamon.model.field.terrain.Terrain
import it.unibo.skalamon.model.field.weather.kind.Sunny
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FieldBuilderTest extends AnyFlatSpec with should.Matchers:

  val bob: Trainer = Trainer("Bob")
  val alice: Trainer = Trainer("Alice")

  "Field Builder" should "let create empty field" in:
    val empty: Field = field(bob :: Nil)(_ => ())
    println(empty)
    empty shouldBe Field(Map((bob -> FieldSide())), None, None, None)

  it should "let create field with Terrain" in:
    val mudField: Field = field(bob :: Nil) { b =>
      b.setTerrain(Terrain("Mud"))
    }
    mudField shouldBe Field(
      Map((bob -> FieldSide())),
      Some(Terrain("Mud")),
      None,
      None
    )

  it should "let create field with Room" in:
    val darkField: Field = field(bob :: Nil) { b =>
      b.setRoom(Room("Dark"))
    }
    darkField shouldBe Field(
      Map((bob -> FieldSide())),
      None,
      Some(Room("Dark")),
      None
    )

  it should "let create field with Weather" in:
    val foggyField: Field = field(bob :: alice :: Nil) { b =>
      b.setWeather(Sunny(1))
    }
    foggyField shouldBe Field(
      Map((bob -> FieldSide()), (alice -> FieldSide())),
      None,
      None,
      Some(Sunny(1))
    )

  it should "let create combinations" in:
    val foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain(Terrain("Mud"))
      b.setWeather(Sunny(2))
      b.setRoom(Room("Dark"))
    }
    foggyMudAndDark shouldBe Field(
      Map((alice -> FieldSide()), (bob -> FieldSide())),
      Some(Terrain("Mud")),
      Some(Room("Dark")),
      Some(Sunny(2))
    )

  it should "let change side" in:
    val foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain(Terrain("Mud"))
      b.setWeather(Sunny(3))
      b.setRoom(Room("Dark"))
    }
    object SimpleFieldSide extends FieldSide
    foggyMudAndDark.changeSide(alice, SimpleFieldSide)
    foggyMudAndDark shouldBe Field(
      Map((alice -> SimpleFieldSide), (bob -> FieldSide())),
      Some(Terrain("Mud")),
      Some(Room("Dark")),
      Some(Sunny(3))
    )
