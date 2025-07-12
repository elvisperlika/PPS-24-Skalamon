package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.{Physical, Special}
import it.unibo.skalamon.model.status.*
import it.unibo.skalamon.model.status.nonVolatileStatus.{Burn, Sleep}
import it.unibo.skalamon.model.status.volatileStatus.{
  Flinch,
  ProtectEndure,
  Yawn
}
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.Electric

/** Test utilities for testing Pokémon.
  */
object PokemonTestUtils:
  private val moveThunderShock = Move(
    name = "Thunder Shock",
    priority = 5,
    moveType = Electric,
    category = Special,
    pp = 5,
    accuracy = Of(100.percent),
    success = _ => EmptyBehavior
  )
  private val moveElectric = Move(
    name = "Electric",
    priority = 5,
    moveType = Electric,
    category = Physical,
    pp = 5,
    accuracy = Of(100.percent),
    success = _ => EmptyBehavior
  )

  private val startingHP: Int = 70
  private val powerPoint: Int = 4

  private val firstTurn: Int = 1
  private val midTurn: Int = 4
  private val lateTurn: Int = 8

  val simplePokemon1: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn(), firstTurn)),
    Set(
      AssignedStatus(Flinch(), midTurn),
      AssignedStatus(ProtectEndure(), midTurn)
    )
  )

  /** This Pokémon is KO.
    */
  val simplePokemon1ko: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    currentHP = 0,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn(), firstTurn)),
    Set(
      AssignedStatus(Flinch(), midTurn),
      AssignedStatus(ProtectEndure(), midTurn)
    )
  )

  /** This Pokémon has no NonVolatileStatus and no VolatileStatus.
    */
  val simplePokemon2: BattlePokemon = BattlePokemon(
    Pokemon.bulbasaur,
    Male,
    startingHP,
    List(BattleMove(moveElectric, powerPoint)),
    Option.empty,
    Set.empty
  )

  val simplePokemon3: BattlePokemon = BattlePokemon(
    Pokemon.charmander,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Sleep(), midTurn)),
    Set(
      AssignedStatus(Flinch(), midTurn),
      AssignedStatus(ProtectEndure(), midTurn),
      AssignedStatus(Yawn(), lateTurn)
    )
  )

  /** This Pokémon has only VolatileStatus (Flinch, ProtectEndure, Yawn) and no
    * NonVolatileStatus.
    */
  val simplePokemon4: BattlePokemon = BattlePokemon(
    Pokemon.gyarados,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option.empty,
    Set(
      AssignedStatus(Flinch(firstTurn), firstTurn),
      AssignedStatus(ProtectEndure(firstTurn), firstTurn),
      AssignedStatus(Yawn(firstTurn), firstTurn)
    )
  )

  /** This Pokémon has only a NonVolatileStatus (Burn) and no VolatileStatus.
    */
  val simplePokemon5: BattlePokemon = BattlePokemon(
    Pokemon.gyarados,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn(), midTurn)),
    Set.empty
  )

  val simplePokemon6: BattlePokemon = BattlePokemon(
    Pokemon.squirtle,
    Female,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Sleep(), midTurn)),
    Set(
      AssignedStatus(Flinch(), midTurn),
      AssignedStatus(ProtectEndure(), midTurn),
      AssignedStatus(Yawn(), lateTurn)
    )
  )

  def trainerAlice: Trainer =
    Trainer("Alice", List(simplePokemon1), _inField = Some(simplePokemon1))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2), _inField = Some(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3), _inField = Some(simplePokemon3))

  def trainerKirk: Trainer =
    Trainer(
      "Kirk",
      List(simplePokemon4, simplePokemon6),
      _inField = Some(simplePokemon4)
    )

  def trainerLuca: Trainer =
    Trainer("Luca", List(simplePokemon5), _inField = Some(simplePokemon5))
