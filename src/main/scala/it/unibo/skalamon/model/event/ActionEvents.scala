package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}

/** Events related to actions performed in a battle.
  */
object ActionEvents:
  /** Event that notifies the execution of a move */
  object Move extends EventType[MoveAction]
  /** Event that notifies the execution of a Pokémon switch */
  object Switch extends EventType[SwitchAction]
