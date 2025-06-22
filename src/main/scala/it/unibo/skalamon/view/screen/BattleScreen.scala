package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*
import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class BattleScreen(
    terminal: AsciiPanel
) extends Screen:
  import BattleScreen.*

  val playerNumber: Int = 2

  def setPlayersName(
      player: String,
      opponent: String
  ): Unit =
    terminal.writeCenter(opponent, opponentNameY)
    terminal.writeCenter(player, playerNameY)

  private def setBattlePokemon(
      terminal: AsciiPanel
  ): Unit =
    val battlePokemonText: Seq[String] =
      Seq("TODO")

    val p1BattlePokemon = HorizontalContainer(
      terminal,
      battlePokemonText,
      p1AbilitiesY + abilitySlotHeight,
      battlePokemonSlotNum,
      battlePokemonWidth,
      battlePokemonHeight
    )

    val p2BattlePokemon = HorizontalContainer(
      terminal,
      battlePokemonText,
      p1BattlePokemonY + battlePokemonHeight + playerPadding,
      battlePokemonSlotNum,
      battlePokemonWidth,
      battlePokemonHeight
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

  override def respondToUserInput(key: KeyEvent): Screen =
    this

object BattleScreen:
  val opponentNameY = 1
  val startY = 3
  val playerPadding = 1

  // Pokemon slots
  val pokemonSlotNum = 5
  val pokemonSlotWidth = 14
  val pokemonSlotHeight = 3

  // Abilities
  val abilitySlotNum = 4
  val abilitySlotWidth = 18
  val abilitySlotHeight = 8

  // Battle Pokemon
  val battlePokemonSlotNum = 1
  val battlePokemonWidth = 46
  val battlePokemonHeight = 3

  // Layout positions (computed)
  val p1PokemonY = startY
  val p1AbilitiesY = p1PokemonY + pokemonSlotHeight
  val p1BattlePokemonY = p1AbilitiesY + abilitySlotHeight

  val p2BattlePokemonY = p1BattlePokemonY + battlePokemonHeight + playerPadding
  val p2AbilitiesY = p2BattlePokemonY + battlePokemonHeight
  val p2PokemonY = p2AbilitiesY + abilitySlotHeight

  val playerNameY = p2PokemonY + pokemonSlotHeight + opponentNameY
