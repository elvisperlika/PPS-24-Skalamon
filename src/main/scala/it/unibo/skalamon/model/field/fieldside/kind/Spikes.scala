package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.field.fieldside.{Multi, SideCondition}

import scala.reflect.ClassTag

case class Spikes(c: Int)(implicit val classTag: ClassTag[Spikes])
    extends SideCondition
    with Multi[Spikes](Spikes.Limit)
    with Expirable(creationTurn = c):
  override val duration: Int = Spikes.Duration

object Spikes:
  private val Duration: Int = 5
  private val Limit: Int = 3
  def apply(creationTurn: Int)(implicit classTag: ClassTag[Spikes]): Spikes =
    new Spikes(creationTurn)(classTag)
