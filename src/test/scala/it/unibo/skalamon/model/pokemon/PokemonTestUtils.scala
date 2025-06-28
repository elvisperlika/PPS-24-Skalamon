package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model
import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.move.*
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
  private val moveThunderShock = Move("Thunder Shock")
  private val moveElectric = Move("Electric")

  private val startingHP: Int = 70
  private val powerPoint: Int = 4

  val simplePokemon1: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn, 1)),
    Set(
      AssignedStatus(Substitute, 4),
      AssignedStatus(ProtectEndure, 3),
      AssignedStatus(Substitute, 8)
    )
  )

  /** This Pokémon is KO.
    */
  val simplePokemon1ko: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    currentHP = 0,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Burn, 1)),
    Set(
      AssignedStatus(Substitute, 4),
      AssignedStatus(ProtectEndure, 3),
      AssignedStatus(Substitute, 8)
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

  private val simplePokemon3: BattlePokemon = BattlePokemon(
    Pokemon.charmander,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    Option(AssignedStatus(Sleep, 4)),
    Set(
      AssignedStatus(Flinch, 4),
      AssignedStatus(ProtectEndure, 3),
      AssignedStatus(Yawn, 8)
    )
  )

  def trainerAlice: Trainer =
    Trainer("Alice", List(simplePokemon1))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3))
