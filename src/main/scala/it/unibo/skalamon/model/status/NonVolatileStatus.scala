package it.unibo.skalamon.model.status

trait NonVolatileStatus extends Status

case class Burn(turns: Int) extends NonVolatileStatus

case class Paralyze(turns: Int) extends NonVolatileStatus

case class Sleep(turns: Int) extends NonVolatileStatus

case class Freeze(turns: Int) extends NonVolatileStatus

case class Poison(turns: Int) extends NonVolatileStatus

case class BadlyPoison(turns: Int) extends NonVolatileStatus
