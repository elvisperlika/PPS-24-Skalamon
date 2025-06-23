package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.behavior.visitor.BattleStateUpdaterBehaviorVisitor
import it.unibo.skalamon.model.event.{BattleStateEvents, EventManager}
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

  /** Applies the behaviors in this context to the given battle state.
    *
    * A [[BattleStateEvents.Changed]] event is also emitted for each behavior
    * applied, if an `EventManager` is provided.
    *
    * @param state
    *   The current battle state to be updated.
    * @param eventManager
    *   The event manager to notify about changes in the battle state.
    * @return
    *   A new battle state with the behaviors applied.
    */
  def apply(state: BattleState)(using
      eventManager: EventManager = EventManager()
  ): BattleState =
    behaviors.foldLeft(state) { case (currentState, (behavior, modifiers)) =>
      val visitor =
        BattleStateUpdaterBehaviorVisitor(currentState, this, modifiers)
      val newState = behavior.accept(visitor)
      eventManager.notify(BattleStateEvents.Changed of (
        currentState,
        newState
      ))
      newState
    }
