package it.unibo.skalamon.model.move

import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.TargetModifier.Type.Self
import it.unibo.skalamon.model.behavior.modifier.{
  BehaviorGroup,
  ProbabilityModifier,
  RandomModifier,
  TargetModifier
}
import it.unibo.skalamon.model.behavior.{Behavior, EmptyBehavior}
import it.unibo.skalamon.model.data.percent
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.field.weather.{Rain, Sunny}
import it.unibo.skalamon.model.move.MoveModel.Category.*
import it.unibo.skalamon.model.move.MoveModel.{Accuracy, Category}
import it.unibo.skalamon.model.pokemon.Stat.{
  Attack,
  Defense,
  SpecialDefense,
  Speed
}
import it.unibo.skalamon.model.status.{BadlyPoison, Burn, Paralyze, Sleep}
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

  // Normal

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

  def swordDance: Move =
    import it.unibo.skalamon.model.behavior.kind.+
    move("Sword Dance", Normal, Status):
      _ pp 20 onSuccess new StatChangeBehavior(Attack + 2)
        with TargetModifier(Self)

  def growl: Move =
    move("Growl", Normal, Status):
      _ pp 40 onSuccess StatChangeBehavior(Attack - 1)

  def superFang: Move =
    move("Super Fang", Normal, Physical):
      _ pp 10 onSuccess: context =>
        DamageBehavior(context.target.currentHP / 2)

  // Dark

  def bite: Move =
    move("Bite", Dark, Physical):
      _ pp 25 onSuccess SingleHitBehavior(60)

  def nightSlash: Move =
    move("Night Slash", Dark, Physical):
      _ pp 15 onSuccess SingleHitBehavior(70)

  // Electric

  def thunderbolt: Move =
    move("Thunderbolt", Electric, Special):
      _ pp 15 onSuccess groupOf(
        SingleHitBehavior(90),
        new StatusBehavior(_ => Paralyze) with ProbabilityModifier(10.percent)
      )

  def thunderWave: Move =
    move("Thunder Wave", Electric, Status):
      _ pp 20 onSuccess StatusBehavior(_ => Paralyze)

  // Dragon

  def dragonRage: Move =
    move("Dragon Rage", Dragon, Special):
      _ pp 10 onSuccess DamageBehavior(40)

  def dragonClaw: Move =
    move("Dragon Claw", Dragon, Physical):
      _ pp 15 onSuccess SingleHitBehavior(80)

  def dragonDance: Move =
    import it.unibo.skalamon.model.behavior.kind.+
    move("Dragon Dance", Dragon, Status):
      _ pp 20 onSuccess new BehaviorGroup(
        StatChangeBehavior(Attack + 1),
        StatChangeBehavior(Speed + 1)
      ) with TargetModifier(Self)

  // Flying

  def roost: Move =
    move("Roost", Flying, Status):
      _ pp 10 onSuccess: context =>
        new HealBehavior(context.source.base.hp - context.source.currentHP)
          with TargetModifier(Self)

  // Water

  def surf: Move =
    move("Surf", Water, Special):
      _ pp 15 onSuccess SingleHitBehavior(90)

  def aquaJet: Move =
    move("Aqua Jet", Water, Physical):
      _ pp 20 priority 1 onSuccess SingleHitBehavior(40)

  def rainDance: Move =
    move("Rain Dance", Water, Status):
      _ pp 5 onSuccess WeatherBehavior(Rain(_))

  // Fire

  def flamethrower: Move =
    move("Flamethrower", Fire, Special):
      _ pp 15 onSuccess groupOf(
        SingleHitBehavior(90),
        new StatusBehavior(_ => Burn) with ProbabilityModifier(10.percent)
      )

  def willOWisp: Move =
    move("Will-O-Wisp", Fire, Status):
      _ pp 15 onSuccess StatusBehavior(_ => Burn)

  def sunnyDay: Move =
    move("Sunny Day", Fire, Status):
      _ pp 5 onSuccess WeatherBehavior(Sunny(_))

  // Ground

  def earthquake: Move =
    move("Earthquake", Ground, Physical):
      _ pp 10 onSuccess SingleHitBehavior(100)

  def fissure: Move =
    move("Fissure", Ground, Physical):
      _ pp 5 accuracyOf 30.percent onSuccess: context =>
        DamageBehavior(context.target.currentHP)

  // Grass

  def grassKnot: Move =
    move("Grass Knot", Grass, Special):
      import scala.math.{log10, max, min}
      _ pp 20 onSuccess: context =>
        val power =
          10 * min(120, max(20, 60 * log10(context.target.base.weightKg) - 40))
        SingleHitBehavior(power.toInt)

  def razorLeaf: Move =
    move("Razor Leaf", Grass, Physical):
      _ pp 25 onSuccess SingleHitBehavior(55)

  def bulletSeed: Move =
    move("Bullet Seed", Grass, Physical):
      _ pp 30 onSuccess: context =>
        RandomModifier(2, 5)(_ => SingleHitBehavior(10))

  // Steel

  def bulletPunch: Move =
    move("Bullet Punch", Steel, Physical):
      _ pp 30 priority 1 onSuccess SingleHitBehavior(40)

  // Psychic

  def psychic: Move =
    move("Psychic", Psychic, Special):
      _ pp 10 onSuccess groupOf(
        SingleHitBehavior(90),
        new StatChangeBehavior(SpecialDefense - 1)
          with ProbabilityModifier(30.percent)
      )

  def zenHeadbutt: Move =
    move("Zen Headbutt", Psychic, Physical):
      _ pp 15 onSuccess SingleHitBehavior(80)

  def hypnosis: Move =
    move("Hypnosis", Psychic, Status):
      _ pp 20 onSuccess StatusBehavior(_ => Sleep)

  def rest: Move =
    move("Rest", Psychic, Status):
      _ pp 10 onSuccess: context =>
        new BehaviorGroup(
          HealthBehavior(context.source.base.hp),
          StatusBehavior(_ => Sleep)
        ) with TargetModifier(Self)

  def trickRoom: Move =
    move("Trick Room", Psychic, Status):
      _ pp 5 onSuccess RoomBehavior(TrickRoom(_))

  // Fighting

  def superpower: Move =
    move("Superpower", Fighting, Physical):
      _ pp 5 onSuccess groupOf(
        SingleHitBehavior(120),
        new BehaviorGroup(
          StatChangeBehavior(Attack - 1),
          StatChangeBehavior(Defense - 1)
        ) with TargetModifier(Self)
      )

  // Poison

  def toxic: Move =
    move("Toxic", Poison, Status):
      _ pp 10 onSuccess StatusBehavior(_ => BadlyPoison)

  def poisonJab: Move =
    move("Poison Jab", Poison, Physical):
      _ pp 20 onSuccess groupOf(
        SingleHitBehavior(80),
        new StatusBehavior(_ => it.unibo.skalamon.model.status.Poison) with ProbabilityModifier(30.percent)
      )