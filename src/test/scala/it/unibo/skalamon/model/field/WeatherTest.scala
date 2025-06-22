package it.unibo.skalamon.model.field

import it.unibo.skalamon.model.ability.*
import it.unibo.skalamon.model.behavior.kind.Stats
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

  var pokemon1: BattlePokemon = BattlePokemon(
    base = Pokemon(
      name = "Pikachu",
      gender = Male,
      types = Electric,
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
    level = 1,
    currentHP = 100,
    moves = Nil,
    nonVolatileStatus = None,
    volatileStatus = Nil
  )

  var pokemon2: BattlePokemon = BattlePokemon(
    base = Pokemon(
      name = "Snowsaur",
      gender = Female,
      types = Ice,
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
    level = 5,
    currentHP = 80,
    moves = Nil,
    nonVolatileStatus = None,
    volatileStatus = Nil
  )

  "Weather" should "have types multiplier modifier" in:
    val sunny: Sunny = Sunny(2)
    sunny.typesModifier shouldBe Map(Fire -> 1.5, Water -> 1.5)

  it should "have functions to mutate Pokémon" in:
    val snow: Snow = Snow(5)
    snow.onTurns.foreach(f => pokemon1 = f(pokemon1))
    pokemon1 shouldEqual BattlePokemon(
      base = Pokemon(
        name = "Pikachu",
        gender = Male,
        types = Electric,
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
      level = 1,
      currentHP = 90,
      moves = Nil,
      nonVolatileStatus = None,
      volatileStatus = Nil,
      id = pokemon1.id,
    )
