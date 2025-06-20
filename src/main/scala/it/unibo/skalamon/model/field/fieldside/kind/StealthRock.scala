package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.field.fieldside.{SideCondition, Unique}

import scala.reflect.ClassTag

case class StealthRock(c: Int)(implicit val classTag: ClassTag[StealthRock])
    extends SideCondition with Unique[StealthRock]
    with Expirable(creationTurn = c):
  override val duration: Int = StealthRock.Duration

object StealthRock:
  private val Duration: Int = 5
  def apply(c: Int)(implicit classTag: ClassTag[StealthRock]): StealthRock =
    new StealthRock(c)(classTag)
