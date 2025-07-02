package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.event.config.OrderingUtils
import it.unibo.skalamon.model.move.Move
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.simplePokemon2
import it.unibo.skalamon.model.types.TypesCollection.Electric
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class MoveOrderingTest extends AnyFlatSpec with should.Matchers:

  private val target: Trainer = PokemonTestUtils.trainerAlice.copy(_inField =
    Some(PokemonTestUtils.trainerAlice.team.head)
  )
  private val source: Trainer = PokemonTestUtils.trainerBob.copy(_inField =
    Some(PokemonTestUtils.trainerBob.team.head))

  private val pp = 10

  private val move1 =
    Move(
      "TestMove1",
      priority = 1,
      moveType = Electric,
      category = Physical,
      pp = pp,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battling

  private val move5 =
    Move(
      "TestMove5",
      priority = 5,
      moveType = Electric,
      category = Physical,
      pp = pp,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battling
  
  private val move5b =
    Move(
      "TestMove5b",
      priority = 5,
      moveType = Electric,
      category = Physical,
      pp = pp,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battling
  
  private val move2 =
    Move(
      "TestMove2",
      priority = 2,
      moveType = Electric,
      category = Physical,
      pp = pp,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battling
  
  private val actions = MoveAction(
    move2,
    source,
    target
  ) :: SwitchAction(
    simplePokemon2
  ) :: MoveAction(
    move5,
    source,
    target
  ) :: MoveAction(
    move1,
    source,
    target
  ) :: MoveAction(
    move5b,
    source,
    target
  ) :: Nil

  "Moves" should "be in order" in:
    import OrderingUtils.given
    val flattenList = actions.sorted
    flattenList shouldEqual SwitchAction(
      simplePokemon2
    ) :: MoveAction(
      move5,
      source,
      target
    ) :: MoveAction(
      move5b,
      source,
      target
    ) :: MoveAction(
      move2,
      source,
      target
    ) :: MoveAction(
      move1,
      source,
      target
    ) :: Nil
