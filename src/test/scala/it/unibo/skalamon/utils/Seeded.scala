package it.unibo.skalamon.utils

import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.util.Random

/** Test utility trait that sets a fixed seed for random number generation. */
trait Seeded extends BeforeAndAfterAll:
  this: Suite =>

  private val seed = 0

  override def beforeAll(): Unit =
    super.beforeAll()
    Random.setSeed(seed)
