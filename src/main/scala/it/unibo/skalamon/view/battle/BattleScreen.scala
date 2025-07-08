package it.unibo.skalamon.view.battle

import asciiPanel.AsciiPanel
import it.unibo.skalamon.model.behavior.kind.StatStage
import it.unibo.skalamon.model.field.{Field, FieldEffectMixin}
import it.unibo.skalamon.model.move.BattleMove
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.view.Screen
import it.unibo.skalamon.view.container.*

import java.awt.Color
import java.util.Locale

/** Represents the battle screen in the game. It displays the players' Pokémon,
  * abilities, and other relevant information during a battle.
  *
  * @param terminal
  *   The terminal where the battle screen will be displayed.
  */
class BattleScreen(
    private val terminal: AsciiPanel
) extends Screen(terminal):
  import BattleScreen.*

  private val defaultTerrainName = "No Terrain"
  private val defaultRoomName = "No Room"
  private val defaultWeatherName = "No Weather"

  private val defaultPokemonName = "No Pokemon"
  private val defaultAbilityName = "No Move"

  private val NoStatusText = "No Status"

  private val emptyKey = "-"

  private val statChangeMaxLength = 3

  /** Repaints the battle screen, clearing the terminal and redrawing all
    * components.
    */
  def setTurn(turn: Int): Unit =
    terminal.writeCenter(s"Turn: $turn", turnLineY, turnColor)

  /** Sets the field for the battle screen.
    * @param field
    *   The field to be displayed on the battle screen.
    */
  def setField(field: Field, currentTurn: Int): Unit =
    val terrainText = field.terrain match
      case Some(exp: FieldEffectMixin.Expirable) =>
        s"${field.terrain.getOrElse(defaultTerrainName)} - Turns left: ${exp.turnsLeft(currentTurn)}"
      case _ => defaultTerrainName
    terminal.write(s"Terrain: $terrainText", leftScreenBorder, terrainY)

    val roomText = field.room match
      case Some(exp: FieldEffectMixin.Expirable) =>
        s"${field.room.getOrElse(defaultRoomName)} - Turns left: ${exp.turnsLeft(currentTurn)}"
      case _ => defaultRoomName
    terminal.write(s"Room: $roomText", leftScreenBorder, roomY)

    val weatherText = field.weather match
      case Some(exp: FieldEffectMixin.Expirable) =>
        s"${field.weather.getOrElse(defaultWeatherName)} - Turns left: ${exp.turnsLeft(currentTurn)}"
      case _ => defaultWeatherName
    terminal.write(s"Weather: $weatherText", leftScreenBorder, weatherY)

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
      p1AbilitiesY + abilitySlotHeight
    )
    drawBattlePokemonSlot(
      BoxContainerData(formatBattlePokemon(opponentBP), opponentBPColor),
      p1BattlePokemonY + battlePokemonHeight + playerSidePadding
    )

  /** Sets the Pokémon pool for both players on the screen.
    * @param playerTeam
    *   The list of Battle Pokémon and their keys for the player.
    * @param opponentTeam
    *   The list of Battle Pokémon and their keys for the opponent.
    */
  def setPokemonTeam(
      playerTeam: List[BattlePokemonWithKey],
      opponentTeam: List[BattlePokemonWithKey]
  ): Unit =
    drawTeamSlots(playerTeam, p1PokemonY)
    drawTeamSlots(opponentTeam, p2PokemonY)

  /** Sets the moves for both players on the screen.
    * @param playerMoves
    *   The list of Battle Moves and their keys for the player.
    * @param opponentMoves
    *   The list of Battle Moves and their keys for the opponent.
    */
  def setMoves(
      playerMoves: List[BattleMoveWithKey],
      opponentMoves: List[BattleMoveWithKey]
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
    BoxContainer(
      terminal,
      battlePokemonData,
      centerX(battlePokemonWidth),
      y,
      battlePokemonWidth,
      battlePokemonHeight
    )

  /** Sets the Pokémon slots for a team on the screen.
    * @param team
    *   The list of Battle Pokémon with their keys for the team.
    * @param y
    *   The vertical position where the Pokémon slots will be displayed.
    */
  private def drawTeamSlots(team: List[BattlePokemonWithKey], y: Int): Unit =
    val filledTeam: Seq[BoxContainerData] =
      team.map { case BattlePokemonWithKey(p, key) =>
        if p.currentHP <= 0 then
          BoxContainerData(formatTeam(p, emptyKey), teamEmptyColor)
        else
          BoxContainerData(formatTeam(p, key), teamColor)
      } ++
        Seq.fill(pokemonSlotCount - team.length)(
          BoxContainerData(Seq(defaultPokemonName), teamEmptyColor)
        )

    HorizontalContainer(
      terminal,
      filledTeam,
      y,
      pokemonSlotWidth,
      pokemonSlotHeight
    )

  /** Sets the moves slots for a team on the screen.
    * @param moves
    *   The list of Battle Moves with their keys for the team.
    * @param y
    *   The vertical position where the moves slots will be displayed.
    */
  private def drawMovesSlots(moves: List[BattleMoveWithKey], y: Int): Unit =
    val filledMoves: Seq[BoxContainerData] =
      moves.map { case BattleMoveWithKey(m, key) =>
        BoxContainerData(formatMove(m, key), teamColor)
      } ++
        Seq.fill(abilitySlotCount - moves.length)(
          BoxContainerData(Seq(defaultAbilityName), teamEmptyColor)
        )

    HorizontalContainer(
      terminal,
      filledMoves,
      y,
      abilitySlotWidth,
      abilitySlotHeight
    )

  /** Creates the text for the Battle Pokémon slot.
    * @param bpOpt
    *   An optional Battle Pokémon. If present, it will be formatted; otherwise,
    *   the default Pokémon name will be used.
    * @return
    *   A sequence of strings representing the formatted Battle Pokémon text.
    */
  private def formatBattlePokemon(bpOpt: Option[BattlePokemon]): Seq[String] =
    bpOpt match
      case Some(bp) =>
        val name = bp.base.name
        val hp = s"${bp.currentHP}HP"
        val ability = s"Ability: ${bp.base.ability.name}"
        val status =
          bp.nonVolatileStatus.map(_.status.toString).getOrElse(NoStatusText)
        val statLine = formatStatChanges(bp)

        Seq(s"$name $hp $ability", s"Status: $status", statLine)

      case None =>
        Seq(defaultPokemonName)

  /** Formats the stat changes of a Battle Pokémon.
    * @param bp
    *   The Battle Pokémon whose stat changes are to be formatted.
    * @return
    *   A string representing the formatted stat changes.
    */
  private def formatStatChanges(bp: BattlePokemon): String =
    bp.statChanges
      .filter { case (_, stage) => stage != 0 }
      .map { case (stat, stage) =>
        val statLabel = stat.toString.substring(
          0,
          Math.min(stat.toString.length, statChangeMaxLength)
        )
        val multiplier =
          String.format(Locale.US, "%.1f", StatStage.multiplier(stage))
        s"$statLabel: $multiplier"
      }
      .mkString(" ")

  /** Creates the text for a Battle Move.
    * @param move
    *   The Battle Move to be formatted.
    * @return
    *   A sequence of strings representing the formatted move text.
    */
  private def formatMove(move: BattleMove, key: String): Seq[String] =
    Seq(
      s"${move.move.name}",
      s"${move.move.moveType}",
      s"Cat: ${move.move.category}",
      s"Acc: ${move.move.accuracy.toDisplayString}",
      s"PP: ${move.pp}",
      s"Press: $key"
    )

  private def formatTeam(pokemon: BattlePokemon, key: String): Seq[String] =
    Seq(
      s"${pokemon.base.name}",
      s"Press: $key"
    )

  /** Calculates the center X position for a container based on the terminal
    * width.
    * @param containerWidth
    *   The width of the container to be centered.
    * @return
    *   The X position to center the container in the terminal.
    */
  private def centerX(containerWidth: Int): Int =
    (terminal.getWidthInCharacters - containerWidth) / 2

object BattleScreen:
  /** Number of players in the battle */
  val PlayerCount: Int = 2

  // === Layout constants ===
  private val leftScreenBorder: Int = 0
  private val turnLineY: Int = 1

  private val terrainOffset: Int = 2
  private val roomOffset: Int = 1
  private val weatherOffset: Int = 1

  private val playerNameOffset: Int = 2
  private val playerTeamGap: Int = 2
  private val playerSidePadding: Int = 3

  // === Pokémon team layout ===
  private val pokemonSlotCount: Int = 5
  private val pokemonSlotWidth: Int = 18
  private val pokemonSlotHeight: Int = 4

  // === Ability layout ===
  private val abilitySlotCount: Int = 4
  private val abilitySlotWidth: Int = 22
  private val abilitySlotHeight: Int = 8

  // === Battle Pokémon layout ===
  private val battlePokemonWidth: Int = 54
  private val battlePokemonHeight: Int = 5

  // === Computed Y positions ===
  private val terrainY: Int = turnLineY + terrainOffset
  private val roomY: Int = terrainY + roomOffset
  private val weatherY: Int = roomY + weatherOffset
  private val playerNameY: Int = weatherY + playerNameOffset

  private val p1PokemonY: Int = playerNameY + playerTeamGap
  private val p1AbilitiesY: Int = p1PokemonY + pokemonSlotHeight
  private val p1BattlePokemonY: Int = p1AbilitiesY + abilitySlotHeight

  private val p2BattlePokemonY: Int =
    p1BattlePokemonY + battlePokemonHeight + playerSidePadding
  private val p2AbilitiesY: Int = p2BattlePokemonY + battlePokemonHeight
  private val p2PokemonY: Int = p2AbilitiesY + abilitySlotHeight

  private val opponentNameY: Int =
    p2PokemonY + pokemonSlotHeight + playerTeamGap

  // Colors
  private val turnColor: Color = Color.WHITE
  private val playerColor: Color = Color.WHITE
  private val opponentColor: Color = Color.WHITE

  private val playerBPColor: Color = Color.WHITE
  private val opponentBPColor: Color = Color.WHITE

  private val teamColor: Color = Color.WHITE
  private val teamEmptyColor: Color = Color.GRAY

/** Represents a Battle Move with an associated key for user input.
  * @param move
  *   The Battle Move to be represented.
  * @param key
  *   The key associated with the move for user input.
  */
case class BattleMoveWithKey(move: BattleMove, key: String)

/** Represents a Battle Pokémon with an associated key for user input.
  * @param move
  *   The Battle Pokémon to be represented.
  * @param key
  *   The key associated with the Pokémon for user input.
  */
case class BattlePokemonWithKey(move: BattlePokemon, key: String)
