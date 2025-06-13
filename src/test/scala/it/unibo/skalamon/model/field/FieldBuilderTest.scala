package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.{Room, Terrain, Weather}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FieldBuilderTest extends AnyFlatSpec with should.Matchers:

  "Field Builder" should "let create empty field" in:
    val empty: Field = field(_ => ())
    println(empty)
    empty shouldBe Field(None, None, None)

  it should "let create field with Terrain" in :
    val mudField: Field = field { b =>
      b.terrain("Mud")
    }
    mudField shouldBe Field(Some(Terrain("Mud")), None, None)

  it should "let create field with Room" in :
    val darkField: Field = field { b =>
      b.room("Dark")
    }
    darkField shouldBe Field(None, Some(Room("Dark")), None)

  it should "let create field with Weather" in :
    val foggyField: Field = field { b =>
      b.weather("Foggy")
    }
    foggyField shouldBe Field(None, None, Some(Weather("Foggy")))

  it should "let create combinations" in:
    val foggyMudAndDark: Field = field { b =>
      b.terrain("Mud")
      b.weather("Foggy")
      b.room("Dark")
    }
    foggyMudAndDark shouldBe Field(Some(Terrain("Mud")), Some(Room("Dark")), Some(Weather("Foggy")))