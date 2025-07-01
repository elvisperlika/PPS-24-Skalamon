package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** */
trait Action:
  val priority: Int

/** @param move
  * @param source
  * @param target
  */
case class MoveAction(
    move: BattleMove,
    source: BattlePokemon,
    target: BattlePokemon
) extends Action:
  override val priority: Int = move.move.priority

/** @param pIn
  * @param pOut
  */
case class SwitchAction(pIn: BattlePokemon, pOut: BattlePokemon) extends Action:
  override val priority: Int = SwitchAction.Priority

/** */
object SwitchAction:
  private val Priority: Int = 6
