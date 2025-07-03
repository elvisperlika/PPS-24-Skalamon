package it.unibo.skalamon.view.battle

import java.awt.event.KeyEvent

enum BattleInput:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerMove1, playerMove2, playerMove3, playerMove4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentMove1, opponentMove2, opponentMove3, opponentMove4

case class PokemonBinding(input: BattleInput, keyCode: Int)
case class MoveBinding(input: BattleInput, keyCode: Int)

object BattleKeyBindings:

  private val moveBindings: Map[String, List[MoveBinding]] = Map(
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

  private val pokemonBindings: Map[String, List[PokemonBinding]] = Map(
    "player" -> List(
      PokemonBinding(BattleInput.playerPokemon1, KeyEvent.VK_1),
      PokemonBinding(BattleInput.playerPokemon2, KeyEvent.VK_2),
      PokemonBinding(BattleInput.playerPokemon3, KeyEvent.VK_3),
      PokemonBinding(BattleInput.playerPokemon4, KeyEvent.VK_4),
      PokemonBinding(BattleInput.playerPokemon5, KeyEvent.VK_5)
    ),
    "opponent" -> List(
      PokemonBinding(BattleInput.opponentPokemon1, KeyEvent.VK_A),
      PokemonBinding(BattleInput.opponentPokemon2, KeyEvent.VK_S),
      PokemonBinding(BattleInput.opponentPokemon3, KeyEvent.VK_D),
      PokemonBinding(BattleInput.opponentPokemon4, KeyEvent.VK_F),
      PokemonBinding(BattleInput.opponentPokemon5, KeyEvent.VK_G)
    )
  )

  private val staticKeyBindings: Map[Int, BattleInput] =
    val moveKeyMap = moveBindings.values.flatten.map(b => b.keyCode -> b.input)
    val pokemonKeyMap =
      pokemonBindings.values.flatten.map(b => b.keyCode -> b.input)
    (moveKeyMap ++ pokemonKeyMap).toMap

  def getInput(group: String, index: Int): Option[BattleInput] =
    moveBindings.get(group).flatMap(_.lift(index)).map(_.input)

  def getKeyChar(group: String, index: Int): Option[String] =
    moveBindings.get(group).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head.toString
    )

  def getPokemonInput(group: String, index: Int): Option[BattleInput] =
    pokemonBindings.get(group).flatMap(_.lift(index)).map(_.input)

  def getPokemonKeyChar(group: String, index: Int): Option[String] =
    pokemonBindings.get(group).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head.toString
    )

  def keyEventToBattleInput(event: KeyEvent): Option[BattleInput] =
    staticKeyBindings.get(event.getKeyCode)
