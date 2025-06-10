package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.kind.{
  SimpleSingleHitBehavior,
  SingleHitBehavior
}
import it.unibo.skalamon.model.behavior.modifier.*
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for the creation of a [[MoveContext]] from a [[BattleMove]].
  */
class MoveContextCreationTest extends AnyFlatSpec with should.Matchers:
  private val target = PokemonTestUtils.simplePokemon1
  private val source = PokemonTestUtils.simplePokemon2

  extension (move: Move)
    def battleMove(): BattleMove = BattleMove(move, pp = 10)

  "Move with no behaviors" should "create a context with empty behaviors" in:
    val move =
      Move(
        "TestMove",
        success = MovePhase(List.empty)
      ).battleMove()
    val context = move.createContext(_.success, target, source)

    context.move shouldEqual move
    context.target shouldEqual target
    context.source shouldEqual source
    context.behaviors shouldBe empty

  "Move with one plain behavior" should "create a context with that behavior" in:
    val move =
      Move(
        "TestMove",
        success = MovePhase(List(SingleHitBehavior(10)))
      ).battleMove()
    val context = move.createContext(_.success, target, source)
    context.behaviors shouldEqual List((
      SingleHitBehavior(10),
      BehaviorModifiers()
    ))

  "Move with one modified behavior" should "create a context with that behavior and its modifiers" in:
    val move =
      Move(
        "TestMove",
        success = MovePhase(
          List(
            new SimpleSingleHitBehavior(10)
              with ProbabilityModifier(100.percent)
              with TargetModifier(TargetModifier.Type.Self)
          )
        )
      ).battleMove()

    val context = move.createContext(_.success, target, source)
    context.behaviors shouldEqual List((
      SingleHitBehavior(10),
      BehaviorModifiers(target = Some(TargetModifier.Type.Self))
    ))
