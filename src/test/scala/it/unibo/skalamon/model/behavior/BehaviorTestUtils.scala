package it.unibo.skalamon.model.behavior

import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.data.RandomGenerator
import it.unibo.skalamon.model.move.{Move, MoveContext, MutableMove}
import it.unibo.skalamon.model.pokemon.MutablePokemon

/** Test utilities for testing behaviors.
  */
object BehaviorTestUtils:
  /** A mock context for moves. */
  val context: MoveContext = MoveContext(
    move = MutableMove(
      Move("TestMove"),
      pp = 10
    ),
    target = MutablePokemon(hp = 100),
    source = MutablePokemon(hp = 100)
  )

  /** @return
    *   A random generator that alternates between returning the maximum and
    *   minimum
    */
  def alternatingNumberGenerator(): RandomGenerator =
    new RandomGenerator {
      private var flag = false

      override def nextInt(min: Int, max: Int): Int =
        flag = !flag
        if (flag) max else min
    }

  /** @return
    *   The plain behaviors from the context, without modifiers.
    */
  def getPlainBehaviors(context: MoveContext): List[Behavior] =
    context.behaviors.map(_._1)
