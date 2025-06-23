package it.unibo.skalamon.model.status

trait VolatileStatus extends Status

case object Confusion extends VolatileStatus

case object Flinch extends VolatileStatus

case object Yawn extends VolatileStatus

case object ProtectEndure extends VolatileStatus

case object Substitute extends VolatileStatus
