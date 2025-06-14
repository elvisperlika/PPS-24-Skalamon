package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.field.weather.kind.Sunny
import it.unibo.skalamon.model.types.{Fire, Water}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class WeatherTest extends AnyFlatSpec with should.Matchers:

  "Weather" should "have types multiplier modifier" in:
    val sunny: Sunny = Sunny(2)
    sunny.typesMultiplierMap shouldBe Map((Fire -> 1.5), (Water -> 1.5))
