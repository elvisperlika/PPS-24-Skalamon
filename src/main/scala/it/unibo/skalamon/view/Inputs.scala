package it.unibo.skalamon.view

import java.awt.event.KeyEvent

/** Represents the side of the player in a battle, either Player or Opponent.
  */
enum PlayerSide:
  case Player, Opponent

/** Represents the keywords for inputs.
  */
enum InputKeyWords:
  case playerPokemon1, playerPokemon2, playerPokemon3, playerPokemon4,
    playerPokemon5
  case playerMove1, playerMove2, playerMove3, playerMove4

  case opponentPokemon1, opponentPokemon2, opponentPokemon3, opponentPokemon4,
    opponentPokemon5
  case opponentMove1, opponentMove2, opponentMove3, opponentMove4

/** Represents the inputs used in the game, allowing players to interact with
  * the game world.
  */
trait Inputs:
  /** Returns the key character for a given player side and index.
    * @param side
    *   The side of the player (Player or Opponent).
    * @param index
    *   The index of the Pokémon or move.
    * @return
    *   An Option containing the key character if it exists, otherwise None.
    */
  def getKeyChar(side: PlayerSide, index: Int): Option[String]

  /** Returns the input keyword for a given player side and index.
    * @param side
    *   The side of the player (Player or Opponent).
    * @param index
    *   The index of the Pokémon or move.
    * @return
    *   An Option containing the InputKeyWords if it exists, otherwise None.
    */
  def getInput(side: PlayerSide, index: Int): Option[InputKeyWords]

  /** Converts a KeyEvent to InputKeyWords.
    * @param event
    *   The KeyEvent to be converted.
    * @return
    *   An Option containing the InputKeyWords if the event corresponds to a
    *   valid input, otherwise None.
    */
  def keyEventToKeyWords(event: KeyEvent): Option[InputKeyWords]
