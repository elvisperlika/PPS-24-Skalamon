package it.unibo.skalamon.controller.battle.action

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon

/** Represents an abstract action that a Pokémon can perform during a battle.
  * Each action has an associated priority used to determine execution order.
  */
trait Action:
  val priority: Int

/** Represents a move action performed by a Pokémon during its turn.
  *
  * @param move
  *   The [[BattleMove]] being executed
  * @param source
  *   The attacking [[Trainer]]
  * @param target
  *   The defending [[Trainer]]
  */
case class MoveAction(
    move: BattleMove,
    source: Trainer,
    target: Trainer
) extends Action:
  /** Priority is determined by the move's defined priority value. */
  override val priority: Int = move.move.priority

/** Represents a switch action where one Pokémon is replaced by another.
  * @param in
  *   The Pokémon entering the battle.
  */
case class SwitchAction(in: BattlePokemon) extends Action:
  /** Priority is fixed for all switch actions. */
  override val priority: Int = SwitchAction.Priority

/** Companion object for [[SwitchAction]] containing constants and utilities.
  */
object SwitchAction:
  /** Default priority for switch actions (higher than most moves). */
  private val Priority: Int = 6
