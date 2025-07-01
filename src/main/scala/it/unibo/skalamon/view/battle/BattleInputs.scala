package it.unibo.skalamon.view.battle

import java.awt.event.KeyEvent

enum BattleInput:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerMove1, playerMove2, playerMove3, playerMove4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentMove1, opponentMove2, opponentMove3, opponentMove4

def keyEventToBattleInput(keyEvent: KeyEvent): Option[BattleInput] =
  keyEvent.getKeyCode match

    case KeyEvent.VK_Q => Some(BattleInput.playerMove1)
    case KeyEvent.VK_W => Some(BattleInput.playerMove2)
    case KeyEvent.VK_E => Some(BattleInput.playerMove3)
    case KeyEvent.VK_R => Some(BattleInput.playerMove4)

    case KeyEvent.VK_Z => Some(BattleInput.opponentMove1)
    case KeyEvent.VK_X => Some(BattleInput.opponentMove2)
    case KeyEvent.VK_C => Some(BattleInput.opponentMove3)
    case KeyEvent.VK_V => Some(BattleInput.opponentMove4)

    case _ => None
