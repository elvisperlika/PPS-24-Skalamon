package it.unibo.skalamon.view.battle

import it.unibo.skalamon.view.{InputKeyWords, Inputs, PlayerSide}

import java.awt.event.KeyEvent

/** Represents a binding between a BattleInput and a key code for Pokémon and
  * moves in a battle.
  * @param input
  *   The BattleInput associated with the key code.
  * @param keyCode
  *   The key code that triggers the BattleInput action.
  */
case class PokemonBinding(input: InputKeyWords, keyCode: Int)

/** Represents a binding between a BattleInput and a key code for moves in a
  * battle.
  * @param input
  *   The BattleInput associated with the key code.
  * @param keyCode
  *   The key code that triggers the BattleInput action.
  */
case class MoveBinding(input: InputKeyWords, keyCode: Int)

/** Provides key bindings for battle inputs, including moves and Pokémon
  * selections for both players and opponents.
  */
object BattleKeyBindings extends Inputs:

  private val moveBindings: Map[PlayerSide, List[MoveBinding]] = Map(
    PlayerSide.Player -> List(
      MoveBinding(InputKeyWords.playerMove1, KeyEvent.VK_Q),
      MoveBinding(InputKeyWords.playerMove2, KeyEvent.VK_W),
      MoveBinding(InputKeyWords.playerMove3, KeyEvent.VK_E),
      MoveBinding(InputKeyWords.playerMove4, KeyEvent.VK_R)
    ),
    PlayerSide.Opponent -> List(
      MoveBinding(InputKeyWords.opponentMove1, KeyEvent.VK_Z),
      MoveBinding(InputKeyWords.opponentMove2, KeyEvent.VK_X),
      MoveBinding(InputKeyWords.opponentMove3, KeyEvent.VK_C),
      MoveBinding(InputKeyWords.opponentMove4, KeyEvent.VK_V)
    )
  )

  private val pokemonBindings: Map[PlayerSide, List[PokemonBinding]] = Map(
    PlayerSide.Player -> List(
      PokemonBinding(InputKeyWords.playerPokemon1, KeyEvent.VK_1),
      PokemonBinding(InputKeyWords.playerPokemon2, KeyEvent.VK_2),
      PokemonBinding(InputKeyWords.playerPokemon3, KeyEvent.VK_3),
      PokemonBinding(InputKeyWords.playerPokemon4, KeyEvent.VK_4),
      PokemonBinding(InputKeyWords.playerPokemon5, KeyEvent.VK_5)
    ),
    PlayerSide.Opponent -> List(
      PokemonBinding(InputKeyWords.opponentPokemon1, KeyEvent.VK_A),
      PokemonBinding(InputKeyWords.opponentPokemon2, KeyEvent.VK_S),
      PokemonBinding(InputKeyWords.opponentPokemon3, KeyEvent.VK_D),
      PokemonBinding(InputKeyWords.opponentPokemon4, KeyEvent.VK_F),
      PokemonBinding(InputKeyWords.opponentPokemon5, KeyEvent.VK_G)
    )
  )

  private val staticKeyBindings: Map[Int, InputKeyWords] =
    val moveKeyMap = moveBindings.values.flatten.map(b => b.keyCode -> b.input)
    val pokemonKeyMap =
      pokemonBindings.values.flatten.map(b => b.keyCode -> b.input)
    (moveKeyMap ++ pokemonKeyMap).toMap

  override def getInput(side: PlayerSide, index: Int): Option[InputKeyWords] =
    moveBindings.get(side).flatMap(_.lift(index)).map(_.input)

  override def getKeyChar(side: PlayerSide, index: Int): Option[String] =
    moveBindings.get(side).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head.toString
    )

  /** Retrieves the key character associated with a Pokémon selection for a
    * given side and index.
    * @param side
    *   The side of the player, either Player or Opponent.
    * @param index
    *   The index of the Pokémon input to retrieve.
    * @return
    *   An Option containing the key character if it exists, otherwise None.
    */
  def getPokemonKeyChar(side: PlayerSide, index: Int): Option[String] =
    pokemonBindings.get(side).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head.toString
    )

  override def keyEventToKeyWords(event: KeyEvent): Option[InputKeyWords] =
    staticKeyBindings.get(event.getKeyCode)
