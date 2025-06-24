package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable

trait VolatileStatus extends Status

case object Confusion extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 3)

case object Flinch extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 0)

case object Yawn extends VolatileStatus,
      Expirable(creationTurn = 0, duration = 0)

case object ProtectEndure extends VolatileStatus

case object Substitute extends VolatileStatus
