package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.model.battle.Trainer

/** Represents the current state of a battle.
  */
enum GameState:

  /** The battle is still ongoing.
    */
  case InProgress

  /** The battle has ended.
    *
    * @param w
    *   An optional winning [[Trainer]]. It is `None` in case of a draw.
    */
  case GameOver(w: Option[Trainer])
