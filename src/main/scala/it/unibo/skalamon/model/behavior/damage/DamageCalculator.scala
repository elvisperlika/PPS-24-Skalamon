package it.unibo.skalamon.model.behavior.damage

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Pokemon}

trait DamageCalculator:

  /** Calculate the move's damage in function of the [[Move]], [[Pokemon]]s and
    * [[BattleState]] statistics.
    * @param origin
    *   Move performed
    * @param source
    *   Pokémon that perform the move
    * @param target
    *   Pokémon that recive the move
    * @param power
    *   Behavior's base damage
    * @param battleState
    *   Current battle state
    * @return
    *   Final damage
    */
  def calculate(
      origin: BattleMove,
      source: BattlePokemon,
      target: BattlePokemon,
      power: Int,
      battleState: BattleState
  ): Int
