package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.room.Room
import it.unibo.skalamon.model.field.expirable.Terrain
import it.unibo.skalamon.model.field.expirable.weather.Weather
import it.unibo.skalamon.model.field.fieldside.FieldSide
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
      b.setTerrain("Mud")
    }
    mudField shouldBe Field(
      Map((bob -> FieldSide())),
      Some(Terrain("Mud")),
      None,
      None
    )

  it should "let create field with Room" in:
    val darkField: Field = field(bob :: Nil) { b =>
      b.setRoom("Dark")
    }
    darkField shouldBe Field(
      Map((bob -> FieldSide())),
      None,
      Some(Room("Dark")),
      None
    )

  it should "let create field with Weather" in:
    val foggyField: Field = field(bob :: alice :: Nil) { b =>
      b.setWeather("Foggy")
    }
    foggyField shouldBe Field(
      Map((bob -> FieldSide()), (alice -> FieldSide())),
      None,
      None,
      Some(Weather("Foggy"))
    )

  it should "let create combinations" in:
    val foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain("Mud")
      b.setWeather("Foggy")
      b.setRoom("Dark")
    }
    foggyMudAndDark shouldBe Field(
      Map((alice -> FieldSide()), (bob -> FieldSide())),
      Some(Terrain("Mud")),
      Some(Room("Dark")),
      Some(Weather("Foggy"))
    )

  it should "let change side" in:
    val foggyMudAndDark: Field = field(alice :: bob :: Nil) { b =>
      b.setTerrain("Mud")
      b.setWeather("Foggy")
      b.setRoom("Dark")
    }
    object SimpleFieldSide extends FieldSide
    foggyMudAndDark.changeSide(alice, SimpleFieldSide)
    foggyMudAndDark shouldBe Field(
      Map((alice -> SimpleFieldSide), (bob -> FieldSide())),
      Some(Terrain("Mud")),
      Some(Room("Dark")),
      Some(Weather("Foggy"))
    )
