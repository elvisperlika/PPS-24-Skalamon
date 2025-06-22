package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.battle.turn.BattleEvents.PokemonSwitchIn
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin.{Expirable, PokemonRules}
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.field.fieldside.{SideCondition, Unique}
import it.unibo.skalamon.model.types.TypeUtility
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.types.TypesCollection.Rock

import scala.reflect.ClassTag

case class StealthRock(t: Int)(implicit val classTag: ClassTag[StealthRock])
    extends SideCondition with Unique[StealthRock]
    with PokemonRules
    with Expirable(t, StealthRock.Duration):
  override val rules: List[(EventType[_], PokemonRule)] =
    (
      PokemonSwitchIn,
      Modify.all {
        p =>
          p.copy(currentHP =
            /* 13 ~ 1/ 8 */
            p.currentHP - (13.percent of p.currentHP * TypeUtility.calculateMultiplier(
              Rock.computeEffectiveness(p.base.types)
            ).toInt)
          )
      }
    ) :: Nil

object StealthRock:
  private val Duration: Int = 5
  def apply(c: Int)(implicit classTag: ClassTag[StealthRock]): StealthRock =
    new StealthRock(c)(classTag)
