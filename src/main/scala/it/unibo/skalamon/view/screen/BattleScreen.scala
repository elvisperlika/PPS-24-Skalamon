package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*
import asciiPanel.AsciiPanel
import it.unibo.skalamon.model.pokemon.BattlePokemon

import java.awt.event.KeyEvent

/** Represents the battle screen in the game. It displays the players' Pokémon,
  * abilities, and other relevant information during a battle.
  *
  * @param terminal
  *   The terminal where the battle screen will be displayed.
  */
class BattleScreen(
    terminal: AsciiPanel
) extends Screen:
  import BattleScreen.*

  private val defaultPokemonName = "No Pokémon"

  /** Shows the player's and opponent's names on the screen.
    * @param player
    *   The name of the player.
    * @param opponent
    *   The name of the opponent.
    */
  def setPlayersName(
      player: String,
      opponent: String
  ): Unit =
    terminal.writeCenter(opponent, opponentNameY)
    terminal.writeCenter(player, playerNameY)

  /** Shows the player's and opponent's Battle Pokémon on the screen.
    * @param playerBP
    *   The player's Battle Pokémon.
    * @param opponentBP
    *   The opponent's Battle Pokémon.
    */
  def setBattlePokemon(
      playerBP: Option[BattlePokemon],
      opponentBP: Option[BattlePokemon]
  ): Unit =
    val playerBPText =
      playerBP.map(bp => Seq(bp.base.name)).getOrElse(Seq(defaultPokemonName))
    val opponentBPText =
      opponentBP.map(bp => Seq(bp.base.name)).getOrElse(Seq(defaultPokemonName))

    setBattlePokemonSlot(
      playerBPText,
      BattleScreen.p1AbilitiesY + BattleScreen.abilitySlotHeight
    )
    setBattlePokemonSlot(
      opponentBPText,
      BattleScreen.p1BattlePokemonY + BattleScreen.battlePokemonHeight + BattleScreen.playerPadding
    )

  private def setPokemonPool(
      terminal: AsciiPanel
  ): Unit =
    val pokemonTextList: Seq[String] = Seq("TODO")
    val p1Pokemon = HorizontalContainer(
      terminal,
      pokemonTextList,
      startY,
      pokemonSlotNum,
      pokemonSlotWidth,
      pokemonSlotHeight
    )

    val p2Pokemon = HorizontalContainer(
      terminal,
      pokemonTextList,
      p2AbilitiesY + abilitySlotHeight,
      pokemonSlotNum,
      pokemonSlotWidth,
      pokemonSlotHeight
    )

  private def setAbilities(
      terminal: AsciiPanel
  ): Unit =

    val abilityTextList: Seq[String] =
      Seq("Move1", "Electric", "Physical", "50-80%", "5/10pp", "rep w button")
    val p1Abilities = HorizontalContainer(
      terminal,
      abilityTextList,
      p1PokemonY + pokemonSlotHeight,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )

    val p2Abilities = HorizontalContainer(
      terminal,
      abilityTextList,
      p2BattlePokemonY + battlePokemonHeight,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )

  /** Shows the Battle Pokémon slot in a specific position on the screen.
    * @param battlePokemonText
    *   The text to display in the Battle Pokémon slot.
    * @param y
    *   The vertical position where the Battle Pokémon slot will be displayed.
    */
  private def setBattlePokemonSlot(
      battlePokemonText: Seq[String],
      y: Int
  ): Unit =
    HorizontalContainer(
      terminal,
      battlePokemonText,
      y,
      BattleScreen.battlePokemonSlotNum,
      BattleScreen.battlePokemonWidth,
      BattleScreen.battlePokemonHeight
    )

  override def respondToUserInput(key: KeyEvent): Screen =
    this

object BattleScreen:
  /** The number of players in the battle. */
  val playerNumber: Int = 2

  private val opponentNameY = 1
  private val startY = 3
  private val playerPadding = 1

  // Pokemon slots
  private val pokemonSlotNum = 5
  private val pokemonSlotWidth = 14
  private val pokemonSlotHeight = 3

  // Abilities
  private val abilitySlotNum = 4
  private val abilitySlotWidth = 18
  private val abilitySlotHeight = 8

  // Battle Pokemon
  private val battlePokemonSlotNum = 1
  private val battlePokemonWidth = 46
  private val battlePokemonHeight = 3

  // Layout positions (computed)
  private val p1PokemonY = startY
  private val p1AbilitiesY = p1PokemonY + pokemonSlotHeight
  private val p1BattlePokemonY = p1AbilitiesY + abilitySlotHeight

  private val p2BattlePokemonY =
    p1BattlePokemonY + battlePokemonHeight + playerPadding
  private val p2AbilitiesY = p2BattlePokemonY + battlePokemonHeight
  private val p2PokemonY = p2AbilitiesY + abilitySlotHeight

  private val playerNameY = p2PokemonY + pokemonSlotHeight + opponentNameY
