package it.unibo.skalamon.model.behavior.visitor

import it.unibo.skalamon.model.battle.BattleState
import it.unibo.skalamon.model.behavior.BehaviorsContext
import it.unibo.skalamon.model.behavior.damage.{
  DamageCalculator,
  DamageCalculatorGen1
}
import it.unibo.skalamon.model.behavior.kind.*
import it.unibo.skalamon.model.behavior.modifier.BehaviorModifiers
import it.unibo.skalamon.model.move.MoveContext
import it.unibo.skalamon.model.pokemon.BattlePokemon
import it.unibo.skalamon.model.status.{
  AssignedStatus,
  NonVolatileStatus,
  VolatileStatus
}

/** * A visitor that updates the battle state based on the behaviors and their
  * modifiers.
  *
  * @param current
  *   The current battle state to be updated.
  * @param context
  *   The context in which the behaviors are executed.
  * @param modifiers
  *   The modifiers applied to the behaviors.
  */
class BattleStateUpdaterBehaviorVisitor(
    private val current: BattleState,
    override val context: BehaviorsContext[_],
    override val modifiers: BehaviorModifiers
) extends ContextualBehaviorVisitor[BattleState]:

  /** Returns a new battle state with [[target]] updated according to [[map]].
    */
  private def updatePokemon(
      target: BattlePokemon,
      map: BattlePokemon => BattlePokemon
  ): BattleState =
    current.copy(
      trainers = current.trainers.map { trainer =>
        trainer.copy(
          team = trainer.team.map { pokemon =>
            if (pokemon is target) map(pokemon) else pokemon
          }
        )
      }
    )

  /** Returns a new battle state with the target Pokémon updated according to
    * [[map]].
    */
  private def updateTarget(map: BattlePokemon => BattlePokemon): BattleState =
    updatePokemon(target, map)

  // Behaviors

  override def visit(behavior: SingleHitBehavior): BattleState =
    val damage: Int = context match
      case MoveContext(origin, target, source, behaviors, _) =>
        val damageCalculator: DamageCalculator = DamageCalculatorGen1
        damageCalculator.calculate(
          origin,
          target,
          source,
          behavior.power,
          current
        )
      case _ => 0
    updateTarget { pokemon =>
      pokemon.copy(currentHP = pokemon.currentHP - damage)
    }

  override def visit(behavior: HealthBehavior): BattleState =
    updateTarget { pokemon =>
      pokemon.copy(
        currentHP = behavior.newHealth(pokemon.currentHP)
      )
    }

  override def visit(behavior: StatChangeBehavior): BattleState = ???

  override def visit(behavior: StatusBehavior): BattleState = {
    val turnIndex = context.turnIndex
    updateTarget { pokemon =>
      behavior.status(turnIndex) match
        case s: VolatileStatus =>
          pokemon.copy(volatileStatus =
            pokemon.volatileStatus +
              AssignedStatus(s, turnIndex)
          )
        case s: NonVolatileStatus if pokemon.nonVolatileStatus.isEmpty =>
          pokemon.copy(nonVolatileStatus =
            Some(AssignedStatus(s, turnIndex))
          )
        case _ => pokemon
    }
  }

  override def visit(behavior: WeatherBehavior): BattleState =
    current.copy(
      field = current.field.copy(weather = Some(behavior.weather(context.turnIndex)))
    )
