package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.pokemon.*

/** Represents the context of an executable procedure in a battle.
  * @see
  *   MoveContext
  */
trait BehaviorsContext[O] extends WithBehaviors:
  /** The source this context originated from. For example, a
    * [[it.unibo.skalamon.model.move.MoveContext]] is originated from a
    * [[it.unibo.skalamon.model.move.BattleMove]].
    */
  val origin: O

  /** The default Pokémon that is targeted by the behaviors. */
  val target: BattlePokemon

  /** The default Pokémon that is executing the behaviors. */
  val source: BattlePokemon
