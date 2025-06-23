package it.unibo.skalamon.view.screen

import it.unibo.skalamon.view.Container.*
import asciiPanel.AsciiPanel
import it.unibo.skalamon.model.pokemon.BattlePokemon

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
    setBattlePokemonSlot(
      BoxContainerData(formatBattlePokemon(playerBP), playerBPColor),
      BattleScreen.p1AbilitiesY + BattleScreen.abilitySlotHeight
    )
    setBattlePokemonSlot(
      BoxContainerData(formatBattlePokemon(opponentBP), opponentBPColor),
      BattleScreen.p1BattlePokemonY + BattleScreen.battlePokemonHeight + BattleScreen.playerPadding
    )

  /** Sets the Pokémon pool for both players on the screen.
    * @param playerTeam
    *   The list of Battle Pokémon for the player.
    * @param opponentTeam
    *   The list of Battle Pokémon for the opponent.
    */
  def setPokemonPool(
      playerTeam: List[BattlePokemon],
      opponentTeam: List[BattlePokemon]
  ): Unit =
    setTeamSlots(playerTeam, p1PokemonY)
    setTeamSlots(opponentTeam, p2PokemonY)

//  private def setAbilities(
//      terminal: AsciiPanel
//  ): Unit =
//
//    val abilityTextList: Seq[String] =
//      Seq("Move1", "Electric", "Physical", "50-80%", "5/10pp", "rep w button")
//    val p1Abilities = HorizontalContainer(
//      terminal,
//      abilityTextList,
//      p1PokemonY + pokemonSlotHeight,
//      abilitySlotNum,
//      abilitySlotWidth,
//      abilitySlotHeight
//    )
//
//    val p2Abilities = HorizontalContainer(
//      terminal,
//      abilityTextList,
//      p2BattlePokemonY + battlePokemonHeight,
//      abilitySlotNum,
//      abilitySlotWidth,
//      abilitySlotHeight
//    )

  /** Shows the Battle Pokémon slot in a specific position on the screen.
    * @param battlePokemonData
    *   * The data to be displayed in the Battle Pokémon slot.
    * @param y
    *   The vertical position where the Battle Pokémon slot will be displayed.
    */
  private def setBattlePokemonSlot(
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
  private def setTeamSlots(team: List[BattlePokemon], y: Int): Unit =
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
  private val teamBPColor: Color = Color.BLUE
  private val teamEmptyColor: Color = Color.GRAY
