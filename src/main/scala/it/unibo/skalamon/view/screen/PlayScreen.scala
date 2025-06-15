package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*
import java.awt.Color
import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class PlayScreen(
    private val playerName: String,
    private val opponentName: String
) extends Screen:

  import PlayScreen.*

  private def setPlayersName(
      terminal: AsciiPanel,
      player: String,
      opponent: String
  ): Unit =
    terminal.writeCenter(opponent, opponentNameY)
    terminal.writeCenter(player, playerNameY)

    val p1Pokemon = HorizontalContainer(
      terminal,
      startY,
      pokemonSlotNum,
      pokemonSlotWidth,
      pokemonSlotHeight
    )

    val p1Abilities = HorizontalContainer(
      terminal,
      p1PokemonY + pokemonSlotHeight,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )

    val p1BattlePokemon = HorizontalContainer(
      terminal,
      p1AbilitiesY + abilitySlotHeight,
      battlePokemonSlotNum,
      battlePokemonWidth,
      battlePokemonHeight
    )

    val p2BattlePokemon = HorizontalContainer(
      terminal,
      p1BattlePokemonY + battlePokemonHeight + playerPadding,
      battlePokemonSlotNum,
      battlePokemonWidth,
      battlePokemonHeight
    )

    val p2Abilities = HorizontalContainer(
      terminal,
      p2BattlePokemonY + battlePokemonHeight,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )

    val p2Pokemon = HorizontalContainer(
      terminal,
      p2AbilitiesY + abilitySlotHeight,
      pokemonSlotNum,
      pokemonSlotWidth,
      pokemonSlotHeight
    )

  override def displayOutput(terminal: AsciiPanel): Unit =
    setPlayersName(terminal, playerName, opponentName)

  override def respondToUserInput(key: KeyEvent): Screen =
    this

object PlayScreen:
  val opponentNameY = 1
  val startY = 3
  val playerPadding = 1

  // Pokemon slots
  val pokemonSlotNum = 5
  val pokemonSlotWidth = 12
  val pokemonSlotHeight = 3

  // Abilities
  val abilitySlotNum = 4
  val abilitySlotWidth = 16
  val abilitySlotHeight = 5

  // Battle Pokemon
  val battlePokemonSlotNum = 1
  val battlePokemonWidth = 20
  val battlePokemonHeight = 3

  // Layout positions (computed)
  val p1PokemonY = startY
  val p1AbilitiesY = p1PokemonY + pokemonSlotHeight
  val p1BattlePokemonY = p1AbilitiesY + abilitySlotHeight

  val p2BattlePokemonY = p1BattlePokemonY + battlePokemonHeight + playerPadding
  val p2AbilitiesY = p2BattlePokemonY + battlePokemonHeight
  val p2PokemonY = p2AbilitiesY + abilitySlotHeight

  val playerNameY = p2PokemonY + pokemonSlotHeight + opponentNameY
