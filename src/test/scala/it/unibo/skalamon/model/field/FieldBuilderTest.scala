package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.expirable.Weather
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FieldBuilderTest extends AnyFlatSpec with should.Matchers:

  "Field Builder" should "let create field without weather, terrain or room" in:
    val empty: Field = field(_ => ())
    println(empty)
    empty shouldBe Field(None, None, None)

    