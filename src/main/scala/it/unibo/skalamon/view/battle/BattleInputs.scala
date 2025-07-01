package it.unibo.skalamon.view.battle

import java.awt.event.KeyEvent

enum BattleInput:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerAction1, playerAction2, playerAction3, playerAction4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentAction1, opponentAction2, opponentAction3, opponentAction4

def keyEventToBattleInput(keyEvent: KeyEvent): Option[BattleInput] =
  keyEvent.getKeyCode match

    case KeyEvent.VK_Q => Some(BattleInput.playerAction1)
    case KeyEvent.VK_W => Some(BattleInput.playerAction2)
    case KeyEvent.VK_E => Some(BattleInput.playerAction3)
    case KeyEvent.VK_R => Some(BattleInput.playerAction4)
    case KeyEvent.VK_Z => Some(BattleInput.opponentAction1)
    case KeyEvent.VK_X => Some(BattleInput.opponentAction2)
    case KeyEvent.VK_C => Some(BattleInput.opponentAction3)
    case KeyEvent.VK_V => Some(BattleInput.opponentAction4)

    case _ => None
