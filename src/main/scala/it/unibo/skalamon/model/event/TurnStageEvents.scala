package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.battle.{Turn, TurnStage}

/** Events related to the stages of a turn in a battle.
  *
  * These events are used to notify when a turn enters a specific stage.
  * @see
  *   [[TurnStage]]
  */
object TurnStageEvents:
  object Started extends EventType[Turn]
  object WaitingForActions extends EventType[Turn]
  object ActionsReceived extends EventType[Turn]
  object ExecutingActions extends EventType[Turn]
  object Ended extends EventType[Turn]

  def from(stage: TurnStage): EventType[Turn] =
    stage match
      case TurnStage.Started            => Started
      case TurnStage.WaitingForActions  => WaitingForActions
      case TurnStage.ActionsReceived(_) => ActionsReceived
      case TurnStage.ExecutingActions   => ExecutingActions
      case TurnStage.Ended              => Ended
