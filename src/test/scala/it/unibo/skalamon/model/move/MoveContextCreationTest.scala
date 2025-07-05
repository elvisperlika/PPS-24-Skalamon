package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.BehaviorTestUtils.getPlainBehaviors
import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.dsl.*
import it.unibo.skalamon.model.move.MoveModel.Accuracy.Of
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import it.unibo.skalamon.model.types.TypesCollection.Electric
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the creation of a [[MoveContext]] from a [[BattleMove]].
  */
class MoveContextCreationTest extends AnyFlatSpec with should.Matchers:
  private val target = PokemonTestUtils.simplePokemon1
  private val source = PokemonTestUtils.simplePokemon2

  "Move with no behaviors" should "create a context with empty behaviors" in:
    val move = Move(
      name = "TestMove",
      priority = 5,
      moveType = Electric,
      category = Physical,
      pp = 10,
      accuracy = Of(100.percent),
      success = _ => EmptyBehavior
    ).battling

    val context = move.createContext(_.success, target, source)

    context.origin shouldEqual move
    context.target shouldEqual target
    context.source shouldEqual source
    context.behaviors shouldBe empty

  "Move with one plain behavior" should "create a context with that behavior" in:
    val move =
      Move(
        name = "TestMove",
        priority = 5,
        moveType = Electric,
        category = Physical,
        pp = 10,
        accuracy = Of(100.percent),
        success = _ => SingleHitBehavior(10)
      ).battling

    val context = move.createContext(_.success, target, source)
    context.behaviors shouldEqual List((
      SingleHitBehavior(10),
      BehaviorModifiers()
    ))

  "Move with one modified behavior" should "create a context with that behavior and its modifiers" in:
    val move =
      Move(
        name = "TestMove",
        priority = 5,
        moveType = Electric,
        category = Physical,
        pp = 10,
        accuracy = Of(100.percent),
        success = _ =>
          new SimpleSingleHitBehavior(10)
            with ProbabilityModifier(100.percent)
            with TargetModifier(TargetModifier.Type.Self)
      ).battling

    val context = move.createContext(_.success, target, source)
    context.behaviors shouldEqual List((
      SingleHitBehavior(10),
      BehaviorModifiers(target = Some(TargetModifier.Type.Self))
    ))

  "Move with two behaviors" should "create a context with those behaviors" in:
    val move =
      Move(
        name = "TestMove",
        moveType = Electric,
        category = Physical,
        pp = 10,
        accuracy = Of(100.percent),
        priority = 5,
        success = _ => BehaviorGroup(SingleHitBehavior(10), SingleHitBehavior(20))
      ).battling

    val context = move.createContext(_.success, target, source)
    getPlainBehaviors(context) shouldEqual List(
      SingleHitBehavior(10),
      SingleHitBehavior(20)
    )

  "Move with context-depending behaviors" should "be affected" in:
    val move =
      Move(
        name = "TestMove",
        moveType = Electric,
        category = Physical,
        pp = 10,
        accuracy = Of(100.percent),
        priority = 5,
        success = context =>
          if context.source.currentHP < 50 then
            SingleHitBehavior(30)
          else
            SingleHitBehavior(10)
      ).battling

    val context1 = move.createContext(_.success, target, source.copy(currentHP = 40))
    val context2 = move.createContext(_.success, target, source.copy(currentHP = 60))

    getPlainBehaviors(context1) shouldEqual List(SingleHitBehavior(30))
    getPlainBehaviors(context2) shouldEqual List(SingleHitBehavior(10))
