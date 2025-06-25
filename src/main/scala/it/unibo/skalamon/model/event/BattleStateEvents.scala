package it.unibo.skalamon.model.event

import it.unibo.skalamon.model.battle.{BattleState, Trainer}

/** Events related to [[BattleState]].
  */
object BattleStateEvents:
  /** Event emitted when the battle state changes.
    *
    * This event is typically used to notify that a behavior has been applied to
    * the battle state, resulting in a new state.
    *
    * The event carries a tuple containing the previous and new battle states.
    */
  object Changed extends EventType[(BattleState, BattleState)]

  /** Event emitted when battle is concluded.
    *
    * Battle may have a winner as not.
    *
    * The event carries an optional of trainer.
    */
  object Finished extends EventType[Option[Trainer]]
