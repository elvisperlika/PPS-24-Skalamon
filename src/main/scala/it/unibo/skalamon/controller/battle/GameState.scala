package it.unibo.skalamon.controller.battle

enum GameState:
  case InProgress
  case GameOver(w: Trainer)

  /** Check if battle is finished.
    * @return
    *   True if battle is finished
    */
  def isGameOver: Boolean = this match
    case GameOver(w) => true
    case _           => false

  /** Winner getter.
    * @return
    *   Optionally the Winner if battle is finished.
    */
  def getWinner: Option[Trainer] = this match
    case GameOver(w) => Some(w)
    case _           => None
