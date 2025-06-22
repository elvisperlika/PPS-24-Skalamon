package it.unibo.skalamon.model.field.fieldside.kind

import it.unibo.skalamon.model.battle.turn.BattleEvents.PokemonSwitchIn
import it.unibo.skalamon.model.event.EventType
import it.unibo.skalamon.model.field.FieldEffectMixin.{
  Expirable,
  FieldEffect,
  PokemonRules
}
import it.unibo.skalamon.model.field.{Modify, PokemonRule}
import it.unibo.skalamon.model.field.fieldside.{SideCondition, Unique}
import it.unibo.skalamon.model.types.TypeUtility
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.types.TypesCollection.Rock

import scala.reflect.ClassTag

case class StealthRock(t: Int)(implicit val classTag: ClassTag[StealthRock])
    extends SideCondition with Unique[StealthRock]
    with PokemonRules with FieldEffect(t)
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
  override val description: String = StealthRock.Description

object StealthRock:
  val Description: String =
    "Deals damage to the target’s Pokémon when they switch in. The damage depends to the target’s weakness to rock type."
  val Duration: Int = 5
  def apply(c: Int)(implicit classTag: ClassTag[StealthRock]): StealthRock =
    new StealthRock(c)(classTag)
