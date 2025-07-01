package it.unibo.skalamon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Special
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon}
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.Electric

// TEMPORARY

object PokemonTestUtils:
  private val moveThunderShock =
    Move(
      name = "Thunder Shock",
      priority = 10,
      moveType = Electric,
      category = Special,
      accuracy = Of(100.percent),
      success = DamageBehavior(10)
    )
  private val moveElectric =
    Move(name = "Electric", 
      priority = 10,
      moveType = Electric,
      category = Special,
      accuracy = Of(100.percent),
      success = DamageBehavior(5))

  private val genericAbility = Ability(
    "Static",
    hooks = Map(
      TurnStageEvents.Started -> DamageBehavior(
        1
      ) // 1HP damage at the start of the turn
    )
  )
  private val blazeAbility = Ability("Blaze", Map.empty)

  private val startingHP: Int = 70
  private val powerPoint: Int = 4

  val simplePokemon1: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(BattleMove(moveThunderShock, powerPoint)),
    None,
    Set.empty
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
    None,
    Set.empty
  )

  def trainerAlice: Trainer =
    Trainer("Alice", List(simplePokemon1))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3))
