package it.unibo.skalamon.model.data

class FixedRandomGenerator(fixedValue: Int) extends RandomGenerator:
  override def nextInt(min: Int, max: Int): Int = fixedValue
