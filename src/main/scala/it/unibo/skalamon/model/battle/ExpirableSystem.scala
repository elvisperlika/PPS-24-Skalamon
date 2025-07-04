package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.field.FieldEffectMixin.Expirable
import it.unibo.skalamon.model.field.fieldside.{FieldSide, SideCondition}
import it.unibo.skalamon.model.field.{Field, FieldEffectMixin}

object ExpirableSystem:

  extension (field: Field)
    /** Remove all expirable effects from the battlefield, like:
      *   - Terrain
      *   - Room
      *   - Weather
      *   - SideCondition
      * @param t
      *   Current turn
      * @return
      *   Field with no expired effects
      */
    def removeExpiredEffects(t: Int): Field =
      /** Return the same Option of the [[Expirable]] status if it's not
        * expired, otherwise None.
        * @param opt
        *   Option of [[Expirable]] element
        * @tparam E
        *   [[Expirable]] effect kind
        * @return
        *   Option of [[E]] if it's not expired
        */
      def cleanEffect[E <: Expirable](opt: Option[E]): Option[E] =
        opt.filterNot(_.isExpired(t))

      /** Remove all expired conditions from a [[FieldSide]]
        * @param f
        *   [[FieldSide]]
        * @param t
        *   Current turn
        * @return
        *   [[FieldSide]] without expired conditions
        */
      def removeExpiredConditions(f: FieldSide, t: Int): FieldSide =
        FieldSide(f.conditions.collect {
          case c: SideCondition with Expirable if !c.isExpired(t) => c
        })

      field.copy(
        sides = field.sides.view.mapValues(removeExpiredConditions(_, t)).toMap,
        terrain = cleanEffect(field.terrain.collect {
          case e: FieldEffectMixin.Terrain with Expirable => e
        }),
        room = cleanEffect(field.room.collect {
          case e: FieldEffectMixin.Room with Expirable => e
        }),
        weather = cleanEffect(field.weather.collect {
          case e: FieldEffectMixin.Weather with Expirable => e
        })
      )
