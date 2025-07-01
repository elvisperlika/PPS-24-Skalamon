package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** Represents an abstract action that a Pokémon can perform during a battle.
  * Each action has an associated priority used to determine execution order.
  */
trait Action:
  val priority: Int

/** Represents a move action performed by a Pokémon during its turn.
  * @param move
  *   The [[BattleMove]] being executed.
  * @param source
  *   The attacking [[BattlePokemon]].
  * @param target
  *   The defending [[BattlePokemon]].
  */
case class MoveAction(
    move: BattleMove,
    source: BattlePokemon,
    target: BattlePokemon
) extends Action:
  /** Priority is determined by the move's defined priority value. */
  override val priority: Int = move.move.priority

/** Represents a switch action where one Pokémon is replaced by another.
  * @param pIn
  *   The Pokémon entering the battle.
  * @param pOut
  *   The Pokémon being withdrawn.
  */
case class SwitchAction(pIn: BattlePokemon, pOut: BattlePokemon) extends Action:
  /** Priority is fixed for all switch actions. */
  override val priority: Int = SwitchAction.Priority

/** Companion object for [[SwitchAction]] containing constants and utilities.
  */
object SwitchAction:
  /** Default priority for switch actions (higher than most moves). */
  private val Priority: Int = 6
