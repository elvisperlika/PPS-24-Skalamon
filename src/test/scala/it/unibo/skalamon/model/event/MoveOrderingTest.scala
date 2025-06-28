package it.unibo.skalamon.model.event

import it.unibo.skalamon.controller.battle.action.{MoveAction, SwitchAction}
import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.event.config.OrderingUtils
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.move.{BattleMove, Move, createContext}
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import it.unibo.skalamon.model.types.TypesCollection.Electric
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import it.unibo.skalamon.model.data.percent

class MoveOrderingTest extends AnyFlatSpec with should.Matchers:

  private val target = PokemonTestUtils.simplePokemon1
  private val source = PokemonTestUtils.simplePokemon2

  extension (move: Move)
    def battleMove(): BattleMove = BattleMove(move, pp = 10)

  val move1 =
    Move(
      "TestMove1",
      priority = 1,
      moveType = Electric,
      category = Physical,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battleMove()
  val context1 = move1.createContext(_.success, target, source)

  val move5 =
    Move(
      "TestMove5",
      priority = 5,
      moveType = Electric,
      category = Physical,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battleMove()
  val context5 = move5.createContext(_.success, target, source)

  val move5b =
    Move(
      "TestMove5b",
      priority = 5,
      moveType = Electric,
      category = Physical,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battleMove()
  val context5b = move5b.createContext(_.success, target, source)

  val move2 =
    Move(
      "TestMove2",
      priority = 2,
      moveType = Electric,
      category = Physical,
      accuracy = Of(100.percent),
      success = EmptyBehavior
    ).battleMove()
  val context2 = move2.createContext(_.success, target, source)

  val actions = MoveAction(
    context2
  ) :: SwitchAction() :: MoveAction(
    context5
  ) :: MoveAction(context1) :: MoveAction(context5b) :: Nil

  "Moves" should "be in order" in:
    import OrderingUtils.given
    val flattenList = actions.sorted

    flattenList shouldEqual SwitchAction() :: MoveAction(
      context5
    ) :: MoveAction(
      context5b
    ) :: MoveAction(
      context2
    ) :: MoveAction(
      context1
    ) :: Nil
