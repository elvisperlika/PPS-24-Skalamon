package it.unibo.skalamon.model.status

trait NonVolatileStatus extends Status

case object Burn extends NonVolatileStatus

case object Paralyze extends NonVolatileStatus

case object Sleep extends NonVolatileStatus

case object Freeze extends NonVolatileStatus

case object Poison extends NonVolatileStatus

case object BadlyPoison extends NonVolatileStatus
