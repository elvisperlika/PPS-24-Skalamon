package it.unibo.skalamon.view.battle

import java.awt.event.KeyEvent

/** Represents the inputs used in a battle, including Pokémon and moves. Each
  * input corresponds to a specific key code for player and opponent actions.
  */
enum BattleInput:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerMove1, playerMove2, playerMove3, playerMove4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentMove1, opponentMove2, opponentMove3, opponentMove4

/** Represents the side of the player in a battle, either Player or Opponent.
  */
enum PlayerSide:
  case Player, Opponent

/** Represents a binding between a BattleInput and a key code for Pokémon and
  * moves in a battle.
  * @param input
  *   The BattleInput associated with the key code.
  * @param keyCode
  *   The key code that triggers the BattleInput action.
  */
case class PokemonBinding(input: BattleInput, keyCode: Int)

/** Represents a binding between a BattleInput and a key code for moves in a
  * battle.
  * @param input
  *   The BattleInput associated with the key code.
  * @param keyCode
  *   The key code that triggers the BattleInput action.
  */
case class MoveBinding(input: BattleInput, keyCode: Int)

/** Provides key bindings for battle inputs, including moves and Pokémon
  * selections for both players and opponents.
  */
object BattleKeyBindings:

  private val moveBindings: Map[PlayerSide, List[MoveBinding]] = Map(
    PlayerSide.Player -> List(
      MoveBinding(BattleInput.playerMove1, KeyEvent.VK_Q),
      MoveBinding(BattleInput.playerMove2, KeyEvent.VK_W),
      MoveBinding(BattleInput.playerMove3, KeyEvent.VK_E),
      MoveBinding(BattleInput.playerMove4, KeyEvent.VK_R)
    ),
    PlayerSide.Opponent -> List(
      MoveBinding(BattleInput.opponentMove1, KeyEvent.VK_Z),
      MoveBinding(BattleInput.opponentMove2, KeyEvent.VK_X),
      MoveBinding(BattleInput.opponentMove3, KeyEvent.VK_C),
      MoveBinding(BattleInput.opponentMove4, KeyEvent.VK_V)
    )
  )

  private val pokemonBindings: Map[PlayerSide, List[PokemonBinding]] = Map(
    PlayerSide.Player -> List(
      PokemonBinding(BattleInput.playerPokemon1, KeyEvent.VK_1),
      PokemonBinding(BattleInput.playerPokemon2, KeyEvent.VK_2),
      PokemonBinding(BattleInput.playerPokemon3, KeyEvent.VK_3),
      PokemonBinding(BattleInput.playerPokemon4, KeyEvent.VK_4),
      PokemonBinding(BattleInput.playerPokemon5, KeyEvent.VK_5)
    ),
    PlayerSide.Opponent -> List(
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

  /** Retrieves the BattleInput associated with a key event.
    * @param side
    *   The side of the player, either Player or Opponent.
    * @param index
    *   The index of the input to retrieve.
    * @return
    *   An Option containing the BattleInput if it exists, otherwise None.
    */
  def getInput(side: PlayerSide, index: Int): Option[BattleInput] =
    moveBindings.get(side).flatMap(_.lift(index)).map(_.input)

  /** Retrieves the key character associated with a BattleInput for a given side
    * and index.
    * @param side
    *   The side of the player, either Player or Opponent.
    * @param index
    *   The index of the input to retrieve.
    * @return
    *   An Option containing the key character if it exists, otherwise None.
    */
  def getKeyChar(side: PlayerSide, index: Int): Option[String] =
    moveBindings.get(side).flatMap(_.lift(index)).map(b =>
      KeyEvent.getKeyText(b.keyCode).head.toString
    )

  /** Retrieves the BattleInput associated with a Pokémon selection for a given
    * side and index.
    * @param side
    *   The side of the player, either Player or Opponent.
    * @param index
    *   The index of the Pokémon input to retrieve.
    * @return
    *   An Option containing the BattleInput if it exists, otherwise None.
    */
  def getPokemonInput(side: PlayerSide, index: Int): Option[BattleInput] =
    pokemonBindings.get(side).flatMap(_.lift(index)).map(_.input)

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

  /** Converts a KeyEvent to a BattleInput if it matches a static key binding.
    * @param event
    *   The KeyEvent to convert.
    * @return
    *   An Option containing the BattleInput if the key code matches a binding,
    */
  def keyEventToBattleInput(event: KeyEvent): Option[BattleInput] =
    staticKeyBindings.get(event.getKeyCode)
