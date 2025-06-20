package it.unibo.skalamon.model.field.fieldside

import scala.reflect.ClassTag

/** Represents the side of the battlefield assigned to a single Pokémon.
  *
  * @param conditions
  *   [[FieldSide]] may be affected by overlapping [[SideCondition2]]s or none
  */
case class FieldSide(
    conditions: List[SideCondition] = Nil
)

extension (field: FieldSide)
  /** Add new condition on the field.
    * @param condition
    *   [[SideCondition]] to add
    * @return
    *   FieldSide updated
    */

  def add(condition: SideCondition | SideCondition with AddConstraint)
      : FieldSide =
    condition match
      case c: SideCondition with AddConstraint =>
        if c.canAdd(field.conditions) then
          field.copy(conditions = c :: field.conditions)
        else field
      case _ => field.copy(conditions = condition :: field.conditions)

/** Represent a state that affect Pokémon in a [[FieldSide]], typically
  * expirable.
  */
trait SideCondition

trait AddConstraint:
  def canAdd(existing: List[SideCondition]): Boolean

trait Unique[E] extends AddConstraint:
  def classTag: ClassTag[E]
  override def canAdd(existing: List[SideCondition]): Boolean =
    !existing.exists(e => classTag.runtimeClass.isInstance(e))

trait Multi[E](limit: Int) extends AddConstraint:
  def classTag: ClassTag[E]
  override def canAdd(existing: List[SideCondition]): Boolean =
    existing.count(e => classTag.runtimeClass.isInstance(e)) < limit
