package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.battle.turn.BattleEvents.PokemonSwitchIn
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.field.fieldside.kind.StealthRock
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon, Stat}
import it.unibo.skalamon.model.types.TypesCollection.Ice
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SideConditionTest extends AnyFlatSpec with should.Matchers:

  var icePokemon: BattlePokemon = BattlePokemon(
    base = Pokemon(
      name = "Pikachu",
      types = Ice :: Nil,
      hp = 100,
      stats = Stats(
        base = Map(
          Stat.Attack -> 100,
          Stat.Defense -> 100,
          Stat.SpecialAttack -> 100,
          Stat.SpecialDefense -> 100,
          Stat.Speed -> 100
        )
      ),
      ability = Ability("Say Hello", List.empty),
      weightKg = 10,
      moves = Nil
    ),
    gender = Male,
    currentHP = 100,
    moves = Nil,
    nonVolatileStatus = None,
    volatileStatus = Set.empty
  )

  "Stealth rock" should "damage pokemon on switching in base of their weaknessto Rock type" in:
    val fieldEvents: EventManager = EventManager()
    var pokemonInBattle = icePokemon :: Nil
    val sideCondition: StealthRock = StealthRock(1)
    sideCondition.rules.foreach((e, r) =>
      fieldEvents.watch(e)(_ => pokemonInBattle = pokemonInBattle.map(r(_)))
    )
    fieldEvents.notify(PokemonSwitchIn of icePokemon)
    /* icePokemon HP is 100: 100 - 13% * Rock weaknesses (Rock -> Ice = 2x) */
    pokemonInBattle shouldEqual icePokemon.copy(currentHP = 74) :: Nil
