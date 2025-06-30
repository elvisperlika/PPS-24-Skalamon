package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.battle.turn.BattleEvents.CreateWeather
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.event.EventManager
import it.unibo.skalamon.model.field.weather.{Snow, Sunny}
import it.unibo.skalamon.model.pokemon.*
import it.unibo.skalamon.model.types.TypesCollection.{
  Electric,
  Fire,
  Ice,
  Water
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class WeatherTest extends AnyFlatSpec with should.Matchers:

  var pikachu: BattlePokemon = BattlePokemon(
    base = Pokemon(
      name = "Pikachu",
      types = Electric,
      hp = 100,
      baseStats = Stats(
        base = Map(
          Stat.Attack -> 100,
          Stat.Defense -> 100,
          Stat.SpecialAttack -> 100,
          Stat.SpecialDefense -> 100,
          Stat.Speed -> 100
        )
      ),
      ability = Ability("Say Hello", Map.empty),
      weightKg = 10,
      possibleMoves = Nil
    ),
    gender = Male,
    currentHP = 100,
    moves = Nil,
    nonVolatileStatus = None,
    volatileStatus = Set.empty
  )

  var snowsaur: BattlePokemon = BattlePokemon(
    base = Pokemon(
      name = "Snowsaur",
      types = Ice,
      hp = 80,
      baseStats = Stats(
        base = Map(
          Stat.Attack -> 90,
          Stat.Defense -> 70,
          Stat.SpecialAttack -> 95,
          Stat.SpecialDefense -> 100,
          Stat.Speed -> 60
        )
      ),
      ability = Ability("Cold Veil", Map.empty),
      weightKg = 85,
      possibleMoves = Nil
    ),
    gender = Female,
    currentHP = 80,
    moves = Nil,
    nonVolatileStatus = None,
    volatileStatus = Set.empty
  )

  "Weather" should "have types multiplier modifier" in:
    val snow: Sunny = Sunny(1)
    snow.typesModifier shouldBe Map(Fire -> 1.5, Water -> 1.5)

  "Snow weather" should "mutate non-Ice Pokémon on weather creation" in:
    val fieldEvents: EventManager = EventManager()
    var pokemonInBattle = pikachu :: snowsaur :: Nil
    val snow: Snow = Snow(5)

    snow.rules.foreach((e, r) =>
      fieldEvents.watch(e)(_ =>
        pokemonInBattle = pokemonInBattle.map(r(_))
      )
    )
    fieldEvents.notify(CreateWeather of snow)
    pokemonInBattle shouldEqual pikachu.copy(currentHP = 90) :: snowsaur :: Nil
