package it.unibo.skalamon.model.status

trait VolatileStatus extends Status

case class Confusion(turns: Int) extends VolatileStatus

case class Flinch(turns: Int) extends VolatileStatus

case class LeechSeed(turns: Int) extends VolatileStatus

case class PerishSong(turns: Int) extends VolatileStatus

case class Yawn(turns: Int) extends VolatileStatus

case class BindMove(turns: Int) extends VolatileStatus

case class AquaRingIngrain(turns: Int) extends VolatileStatus

case class Encore(turns: Int) extends VolatileStatus

case class Taunt(turns: Int) extends VolatileStatus

case class Torment(turns: Int) extends VolatileStatus

case class Disable(turns: Int) extends VolatileStatus

case class Trapped(turns: Int) extends VolatileStatus

case class ProtectEndure(turns: Int) extends VolatileStatus

case class Substitute(turns: Int) extends VolatileStatus

case class FocusEnergy(turns: Int) extends VolatileStatus
