package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.kind.{
  DamageBehavior,
  HealBehavior,
  SingleHitBehavior,
  StatusBehavior
}
import it.unibo.skalamon.model.behavior.modifier.TargetModifier.Type.Self
import it.unibo.skalamon.model.behavior.modifier.{
  ProbabilityModifier,
  TargetModifier
}
import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.move.MoveModel.Category.*
import it.unibo.skalamon.model.move.MoveModel.{Accuracy, Category}
import it.unibo.skalamon.model.status.Paralyze
import it.unibo.skalamon.model.types.Type
import it.unibo.skalamon.model.types.TypesCollection.*

/** A base move, that may belong to a
  * [[it.unibo.skalamon.model.pokemon.Pokemon]] and can be triggered by a
  * [[it.unibo.skalamon.model.pokemon.BattlePokemon]].
  *
  * @param name
  *   The name of the move.
  * @param priority
  *   The priority of the move, which determines the order of execution relative
  *   to other moves.
  * @param moveType
  *   The type of the move (e.g., Fire, Water, etc.).
  * @param category
  *   The category of the move (e.g., Physical, Special, Status).
  * @param accuracy
  *   The accuracy of the move, which determines the likelihood of hitting the
  *   target.
  * @param pp
  *   The Power Points of the move, which indicate how many times the move can
  *   be used before it needs to be restored.
  * @param success
  *   The behavior of this move when it is successful.
  * @param fail
  *   The behavior of this move when it fails/misses.
  */
case class Move(
    name: String,
    priority: Int = 0,
    moveType: Type,
    category: Category,
    accuracy: Accuracy,
    pp: Int,
    success: MoveContext => Behavior = _ => EmptyBehavior,
    fail: MoveContext => Behavior = _ => EmptyBehavior
)

/** A move in the context of a battle.
  *
  * @param move
  *   The base move.
  * @param pp
  *   The current Power Points of the move.
  */
case class BattleMove(move: Move, pp: Int)

/** Factory for moves. */
object Move:
  import it.unibo.skalamon.model.dsl.*

  def tackle: Move =
    move("Tackle", Normal, Physical):
      _ pp 35 onSuccess SingleHitBehavior(40)

  def quickAttack: Move =
    move("Quick Attack", Normal, Physical):
      _ pp 30 priority 1 onSuccess SingleHitBehavior(40)

  def slash: Move =
    move("Slash", Normal, Physical):
      _ pp 20 onSuccess SingleHitBehavior(70)

  def swift: Move =
    move("Swift", Normal, Special):
      _.neverFailing pp 20 onSuccess SingleHitBehavior(60)

  def bite: Move =
    move("Bite", Dark, Physical):
      _ pp 25 onSuccess SingleHitBehavior(60)

  def thunderbolt: Move =
    move("Thunderbolt", Electric, Special):
      _ pp 15 onSuccess groupOf(
        SingleHitBehavior(90),
        new StatusBehavior(_ => Paralyze) with ProbabilityModifier(10.percent)
      )

  def thunderWave: Move =
    move("Thunder Wave", Electric, Status):
      _ pp 20 onSuccess StatusBehavior(_ => Paralyze)

  def dragonRage: Move =
    move("Dragon Rage", Dragon, Special):
      _ pp 10 onSuccess DamageBehavior(40)

  def roost: Move =
    move("Roost", Flying, Status):
      _ pp 10 onSuccess: context =>
        new HealBehavior(context.source.base.hp - context.source.currentHP)
          with TargetModifier(Self)