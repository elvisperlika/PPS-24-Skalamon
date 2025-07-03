package it.unibo.skalamon.view.battle

import java.awt.event.KeyEvent

enum BattleInput:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerMove1, playerMove2, playerMove3, playerMove4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentMove1, opponentMove2, opponentMove3, opponentMove4

case class MoveBinding(input: BattleInput, keyCode: Int)

object BattleKeyBindings:

  val moveBindings: Map[String, List[MoveBinding]] = Map(
    "player" -> List(
      MoveBinding(BattleInput.playerMove1, KeyEvent.VK_Q),
      MoveBinding(BattleInput.playerMove2, KeyEvent.VK_W),
      MoveBinding(BattleInput.playerMove3, KeyEvent.VK_E),
      MoveBinding(BattleInput.playerMove4, KeyEvent.VK_R)
    ),
    "opponent" -> List(
      MoveBinding(BattleInput.opponentMove1, KeyEvent.VK_Z),
      MoveBinding(BattleInput.opponentMove2, KeyEvent.VK_X),
      MoveBinding(BattleInput.opponentMove3, KeyEvent.VK_C),
      MoveBinding(BattleInput.opponentMove4, KeyEvent.VK_V)
    )
  )

  def getInput(group: String, index: Int): Option[BattleInput] =
    moveBindings.get(group).flatMap(_.lift(index)).map(_.input)

  def getKeyChar(group: String, index: Int): Option[Char] =
    moveBindings.get(group).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head
    )

  val staticKeyBindings: Map[Int, BattleInput] = Map(
    KeyEvent.VK_1 -> BattleInput.playerPokemon1,
    KeyEvent.VK_2 -> BattleInput.playerPokemon2,
    KeyEvent.VK_3 -> BattleInput.playerPokemon3,
    KeyEvent.VK_4 -> BattleInput.playerPokemon4,
    KeyEvent.VK_5 -> BattleInput.playerPokemon5,
    KeyEvent.VK_A -> BattleInput.opponentPokemon1,
    KeyEvent.VK_S -> BattleInput.opponentPokemon2,
    KeyEvent.VK_D -> BattleInput.opponentPokemon3,
    KeyEvent.VK_F -> BattleInput.opponentPokemon4,
    KeyEvent.VK_G -> BattleInput.opponentPokemon5
  ) ++ moveBindings.values.flatten.map(b => b.keyCode -> b.input)

  def keyEventToBattleInput(event: KeyEvent): Option[BattleInput] =
    staticKeyBindings.get(event.getKeyCode)
