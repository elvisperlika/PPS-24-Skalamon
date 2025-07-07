package it.unibo.skalamon.model.field.fieldside

import it.unibo.skalamon.model.field.FieldEffectMixin.SideCondition

import scala.reflect.ClassTag

/** Represents the side of the battlefield assigned to a single Pokémon.
  *
  * @param conditions
  *   [[FieldSide]] may be affected by overlapping [[SideCondition]]s or none
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

/** Mixin to create [[SideCondition]] with constraint.
  */
trait AddConstraint:
  def canAdd(existing: List[SideCondition]): Boolean

/** Mixin constraint to create [[SideCondition]] that can't have 
  * same kind of [[SideCondition]] in the same [[FieldSide]].
  * @tparam E
  *   Kind of the [[SideCondition]] created with this mixin
  */
trait Unique[E] extends AddConstraint:
  def classTag: ClassTag[E]
  override def canAdd(existing: List[SideCondition]): Boolean =
    !existing.exists(e => classTag.runtimeClass.isInstance(e))

/** Mixin constraint to create [[SideCondition]] that can be added on
  * [[FieldSide]] until number of them reach the limit.
  * @tparam E
  *   Kind of the [[SideCondition]] created with this mixin
  * @param limit
  *   Number of [[SideCondition]] of type [[E]] are allowed
  */
trait Multi[E](limit: Int) extends AddConstraint:
  def classTag: ClassTag[E]
  override def canAdd(existing: List[SideCondition]): Boolean =
    existing.count(e => classTag.runtimeClass.isInstance(e)) < limit
