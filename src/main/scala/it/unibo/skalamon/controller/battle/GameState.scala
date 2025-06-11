package it.unibo.skalamon.controller.battle

enum GameState:
  case InProgress
  case GameOver(w: Trainer)

  def isGameOver: Boolean = this match 
    case GameOver(w) => true
    case _ => false
