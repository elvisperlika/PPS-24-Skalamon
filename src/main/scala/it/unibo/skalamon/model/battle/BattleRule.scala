package it.unibo.skalamon.model.battle

import it.unibo.skalamon.controller.battle.action.Action
import it.unibo.skalamon.model.event.config.{
  ClassicOrdering,
  InvertedSpeedOrdering
}

/** Defines the fundamental rules of a battle.
  *
  * Each implementation can specify different move ordering strategies.
  */
trait BattleRule:

  /** Ordering used to sort actions during a battle turn.
    *
    * This determines which action is executed first, based on rules such as
    * priority and Pokémon speed (see [[OrderingUtils]]).
    */
  val actionOrderStrategy: Ordering[Action]

/** Classic implementation of battle rules.
  *
  * Uses the default ordering logic defined in [[OrderingUtils]], which sorts
  * actions by priority and speed.
  */
case class Classic() extends BattleRule:

  /** Sorts battle actions using the classic rules:
    *   - Higher priority acts first
    *   - If priority is equal, faster Pokémon acts first
    */
  override val actionOrderStrategy: Ordering[Action] =
    ClassicOrdering.given_Ordering_Action

case class Tricky() extends BattleRule:
  import it.unibo.skalamon.model.event.config.InvertedSpeedOrdering.given

  /** Sorts battle actions using the tricky rules:
    *   - Higher priority acts first
    *   - If priority is equal, the slower Pokémon acts first
    */
  override val actionOrderStrategy: Ordering[Action] =
    InvertedSpeedOrdering.given_Ordering_Action
