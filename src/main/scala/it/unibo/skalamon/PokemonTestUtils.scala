package it.unibo.skalamon

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.move.*
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Special
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon, Stat}
import it.unibo.skalamon.model.types.*
import it.unibo.skalamon.model.types.TypesCollection.Electric

// TEMPORARY

object PokemonTestUtils:
  private val powerPoint: Int = 4

  private val moveThunderShock =
    Move(
      name = "Thunder Shock",
      moveType = Electric,
      category = Special,
      pp = powerPoint,
      accuracy = Of(100.percent),
      success = DamageBehavior(10)
    )
  private val moveElectric =
    Move(
      name = "Electric",
      moveType = Electric,
      category = Special,
      pp = powerPoint,
      accuracy = Of(100.percent),
      success = DamageBehavior(5)
    )

  private val genericAbility = ability("Static"):
    _.on(TurnStageEvents.Started)(_ =>
      DamageBehavior(1)
    ) // 1HP damage at the start of the turn

  private val blazeAbility = Ability("Blaze", List.empty)

  private val startingHP: Int = 70

  val simplePokemon1: BattlePokemon = BattlePokemon(
    Pokemon.pikachu,
    Male,
    startingHP,
    List(
      BattleMove(moveThunderShock, powerPoint),
      BattleMove(moveElectric, powerPoint)
    ),
    None,
    Set.empty,
    statChanges = Map(
      Stat.Attack -> 2,
      Stat.Defense -> -1
    )
  )

  val simplePokemon2: BattlePokemon = BattlePokemon(
    Pokemon.bulbasaur,
    Male,
    startingHP,
    List(
      BattleMove(moveElectric, powerPoint),
      BattleMove(moveThunderShock, powerPoint)
    ),
    Option.empty,
    Set.empty,
    statChanges = Map(
      Stat.Attack -> 0
    )
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
    Trainer("Alice", List(simplePokemon1, simplePokemon3))

  def trainerBob: Trainer =
    Trainer("Bob", List(simplePokemon2))

  def trainerGio: Trainer =
    Trainer("Gio", List(simplePokemon3))
