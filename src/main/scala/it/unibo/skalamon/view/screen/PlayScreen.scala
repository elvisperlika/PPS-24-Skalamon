package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*
import java.awt.Color
import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class PlayScreen(
    private val playerName: String,
    private val opponentName: String
) extends Screen:

  private def setPlayersName(
      terminal: AsciiPanel,
      player: String,
      opponent: String
  ): Unit =
    import PlayScreen.*

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
      startY + pokemonSlotHeight,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )

    val p2AbilitiesY =
      startY + pokemonSlotHeight + abilitySlotHeight + paddingBetweenPlayers
    val p2PokemonY = p2AbilitiesY + abilitySlotHeight

    val p2Abilities = HorizontalContainer(
      terminal,
      p2AbilitiesY,
      abilitySlotNum,
      abilitySlotWidth,
      abilitySlotHeight
    )
    val p2Pokemon = HorizontalContainer(
      terminal,
      p2PokemonY,
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
  val playerNameY = 22

  val startY = 3

  val pokemonSlotNum = 5
  val pokemonSlotWidth = 12
  val pokemonSlotHeight = 3

  val abilitySlotNum = 4
  val abilitySlotWidth = 16
  val abilitySlotHeight = 5

  val paddingBetweenPlayers = 1
