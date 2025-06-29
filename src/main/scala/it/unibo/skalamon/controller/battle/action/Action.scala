package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** */
trait Action:
  val priority: Int

/** @param battleMove
  * @param source
  * @param target
  */
case class MoveAction(
    battleMove: BattleMove,
    source: BattlePokemon,
    target: BattlePokemon
) extends Action:
  override val priority: Int = battleMove.move.priority

/** @param pIn
  * @param pOut
  */
case class SwitchAction(pIn: BattlePokemon, pOut: BattlePokemon) extends Action:
  override val priority: Int = SwitchAction.Priority

/** */
object SwitchAction:
  private val Priority: Int = 6
