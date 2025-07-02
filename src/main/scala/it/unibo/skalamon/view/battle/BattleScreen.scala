package it.unibo.skalamon.view.battle

import asciiPanel.AsciiPanel
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.Container.*
import it.unibo.skalamon.view.Screen
import it.unibo.skalamon.view.screen.BattleScreen

import java.awt.Color
import java.awt.event.KeyEvent

/** Represents the battle screen in the game. It displays the players' Pokémon,
  * abilities, and other relevant information during a battle.
  *
  * @param terminal
  *   The terminal where the battle screen will be displayed.
  */
class BattleScreen(
    private val terminal: AsciiPanel
) extends Screen:
  import BattleScreen.*

  private val defaultPokemonName = "No Pokemon"
  private val defaultAbilityName = "No Move"

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
    terminal.writeCenter(opponent, opponentNameY, opponentColor)
    terminal.writeCenter(player, playerNameY, playerColor)

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
    drawBattlePokemonSlot(
      BoxContainerData(formatBattlePokemon(playerBP), playerBPColor),
      BattleScreen.p1AbilitiesY + BattleScreen.abilitySlotHeight
    )
    drawBattlePokemonSlot(
      BoxContainerData(formatBattlePokemon(opponentBP), opponentBPColor),
      BattleScreen.p1BattlePokemonY + BattleScreen.battlePokemonHeight + BattleScreen.playerPadding
    )

  /** Sets the Pokémon pool for both players on the screen.
    * @param playerTeam
    *   The list of Battle Pokémon for the player.
    * @param opponentTeam
    *   The list of Battle Pokémon for the opponent.
    */
  def setPokemonTeam(
      playerTeam: List[BattlePokemon],
      opponentTeam: List[BattlePokemon]
  ): Unit =
    drawTeamSlots(playerTeam, p1PokemonY)
    drawTeamSlots(opponentTeam, p2PokemonY)

  /** Sets the moves for both players on the screen.
    * @param playerMoves
    *   The list of Battle Moves for the player.
    * @param opponentMoves
    *   The list of Battle Moves for the opponent.
    */
  def setMoves(
      playerMoves: List[BattleMove],
      opponentMoves: List[BattleMove]
  ): Unit =
    drawMovesSlots(playerMoves, p1AbilitiesY)
    drawMovesSlots(opponentMoves, p2AbilitiesY)

  /** Shows the Battle Pokémon slot in a specific position on the screen.
    * @param battlePokemonData
    *   * The data to be displayed in the Battle Pokémon slot.
    * @param y
    *   The vertical position where the Battle Pokémon slot will be displayed.
    */
  private def drawBattlePokemonSlot(
      battlePokemonData: BoxContainerData,
      y: Int
  ): Unit =
    val centerX =
      (terminal.getWidthInCharacters - BattleScreen.battlePokemonWidth) / 2
    BoxContainer(
      terminal,
      battlePokemonData,
      centerX,
      y,
      BattleScreen.battlePokemonWidth,
      BattleScreen.battlePokemonHeight
    )

  /** Sets the Pokémon slots for a team on the screen.
    * @param team
    *   The list of Battle Pokémon for the team.
    * @param y
    *   The vertical position where the Pokémon slots will be displayed.
    */
  private def drawTeamSlots(team: List[BattlePokemon], y: Int): Unit =
    val filledTeam: Seq[BoxContainerData] =
      team.map(p => BoxContainerData(Seq(p.base.name), teamColor)) ++
        Seq.fill(BattleScreen.pokemonSlotNum - team.length)(
          BoxContainerData(Seq(defaultPokemonName), teamEmptyColor)
        )

    HorizontalContainer(
      terminal,
      filledTeam,
      y,
      BattleScreen.pokemonSlotWidth,
      BattleScreen.pokemonSlotHeight
    )

  /** Sets the moves slots for a team on the screen.
    * @param moves
    *   The list of Battle Moves for the team.
    * @param y
    *   The vertical position where the moves slots will be displayed.
    */
  private def drawMovesSlots(moves: List[BattleMove], y: Int): Unit =
    val filledMoves: Seq[BoxContainerData] =
      moves.map(m => BoxContainerData(formatMove(m), teamColor)) ++
        Seq.fill(BattleScreen.abilitySlotNum - moves.length)(
          BoxContainerData(Seq(defaultAbilityName), teamEmptyColor)
        )

    HorizontalContainer(
      terminal,
      filledMoves,
      y,
      BattleScreen.abilitySlotWidth,
      BattleScreen.abilitySlotHeight
    )

  /** Creates the text for the Battle Pokémon slot.
    * @param bpOpt
    *   An optional Battle Pokémon. If present, it will be formatted; otherwise,
    *   the default Pokémon name will be used.
    * @return
    *   A sequence of strings representing the formatted Battle Pokémon text.
    */
  private def formatBattlePokemon(bpOpt: Option[BattlePokemon]): Seq[String] =
    bpOpt
      .map(bp =>
        Seq(
          s"${bp.base.name} ${bp.currentHP}HP Ability: ${bp.base.ability.name}"
        )
      )
      .getOrElse(Seq(defaultPokemonName))

  /** Creates the text for a Battle Move.
    * @param move
    *   The Battle Move to be formatted.
    * @return
    *   A sequence of strings representing the formatted move text.
    */
  private def formatMove(move: BattleMove): Seq[String] =
    Seq(
      s"${move.move.name}",
      s"PP: ${move.pp}"
    )

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

  // Colors
  private val playerColor: Color = Color.WHITE
  private val opponentColor: Color = Color.WHITE

  private val playerBPColor: Color = Color.WHITE
  private val opponentBPColor: Color = Color.WHITE

  private val teamColor: Color = Color.WHITE
  private val teamEmptyColor: Color = Color.GRAY
