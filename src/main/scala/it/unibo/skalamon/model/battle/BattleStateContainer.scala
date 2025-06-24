package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.event.{EventManager, EventType}

/** A container that allows access and manipulation of the current
  * [[BattleState]].
  */
opaque type BattleStateContainer = Turn | Battle

private def currentTurn(t: BattleStateContainer): Turn =
  t match
    case turn: Turn     => turn
    case battle: Battle =>
      battle.currentTurn.getOrElse(
        throw new IllegalStateException("No active turn in battle")
      )

/** Extracts the current [[BattleState]] from the given container.
  * @param t
  *   The container from which to extract the battle state.
  * @return
  *   The current battle state.
  */
def extractBattleState(t: BattleStateContainer): BattleState =
  currentTurn(t).state.snapshot

/** Mutates the current [[BattleState]] in the given container.
  *
  * @param t
  *   The container in which to set the battle state.
  * @param state
  *   The new battle state to set.
  */
def setBattleState(t: BattleStateContainer, state: BattleState): Unit =
  val turn = currentTurn(t)
  turn.state = turn.state.copy(snapshot = state)

extension (eventManager: EventManager)
  /** Hooks a callback to be executed whenever the battle state is updated
    * through the specified event type.
    *
    * When the event is triggered, the callback will receive the current battle
    * state, which can be altered (through an immutable copy) and returned to
    * mutate the battle state within the container.
    *
    * @param eventType
    *   The type of event to watch for.
    * @param callback
    *   The function to execute with the current battle state and the data
    *   associated with the event.
    */
  def hookBattleStateUpdate[T <: BattleStateContainer](eventType: EventType[T])(
      callback: (BattleState, T) => BattleState
  ): Unit =
    eventManager.watch(eventType) { data =>
      val currentState = extractBattleState(data)
      val updatedState = callback(currentState, data)
      setBattleState(data, updatedState)
    }
