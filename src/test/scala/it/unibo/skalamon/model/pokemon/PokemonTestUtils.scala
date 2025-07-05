package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model
import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.{Physical, Special}
import it.unibo.skalamon.model.status.*
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.{
  Electric,
  Fire,
  Grass,
  Poison
}

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

  val simplePokemon1: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn(), 1)),
    Set(
      AssignedStatus(Flinch(), 4),
      AssignedStatus(ProtectEndure(), 3)
    )
  )

  /** This Pokémon is KO.
    */
  val simplePokemon1ko: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    currentHP = 0,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn(), 1)),
    Set(
      AssignedStatus(Flinch(), 4),
      AssignedStatus(ProtectEndure(), 3)
    )
  )

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
    Option(AssignedStatus(Sleep(), 4)),
    Set(
      AssignedStatus(Flinch(), 4),
      AssignedStatus(ProtectEndure(), 3),
      AssignedStatus(Yawn(), 8)
    )
  )

  val simplePokemon4: BattlePokemon = BattlePokemon(
    Pokemon.gyarados,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option.empty,
    Set(
      AssignedStatus(Flinch(), 4),
      AssignedStatus(ProtectEndure(), 3),
      AssignedStatus(Yawn(), 8)
    )
  )

  def trainerAlice: Trainer =
    Trainer("Alice", List(simplePokemon1), _inField = Some(simplePokemon1))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2), _inField = Some(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3), _inField = Some(simplePokemon3))
