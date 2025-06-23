package it.unibo.skalamon

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.kind.Stats
import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.move.{BattleMove, Move}
import it.unibo.skalamon.model.pokemon.{BattlePokemon, Male, Pokemon, Stat}
import it.unibo.skalamon.model.status.*
import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.types.TypesCollection.*
import it.unibo.skalamon.view.*
import it.unibo.skalamon.view.battle.BattleView

@main
def main(): Unit =
  val pokemon1 = BattlePokemon(
    Pokemon(
      "Pikachu",
      Male,
      List(Electric),
      baseStats = Stats(
        base = Map(
          Stat.Attack -> 55,
          Stat.Defense -> 40,
          Stat.SpecialAttack -> 50,
          Stat.SpecialDefense -> 50,
          Stat.Speed -> 90
        )
      ),
      ability = Ability("Blaze", Map.empty),
      weightKg = 6.0,
      possibleMoves = List(Move("Thunder Shock"))
    ),
    100,
    70,
    List(BattleMove(Move("Thunder Shock"), 2)),
    Option(AssignedStatus(Burn, 1)),
    Set(
      AssignedStatus(PerishSong, 4),
      AssignedStatus(AquaRingIngrain, 3),
      AssignedStatus(Substitute, 8)
    )
  )
  val pokemon2 = BattlePokemon(
    Pokemon(
      "Charmander",
      Male,
      Fire,
      baseStats = Stats(
        base = Map(
          Stat.Attack -> 52,
          Stat.Defense -> 43,
          Stat.SpecialAttack -> 60,
          Stat.SpecialDefense -> 50,
          Stat.Speed -> 65
        )
      ),
      ability = Ability("Blaze", Map.empty),
      weightKg = 8.5,
      possibleMoves = List(Move("Thunder Shock"), Move("Electric"))
    ),
    45,
    30,
    List(BattleMove(Move("Thunder Shock"), 4)),
    Option(AssignedStatus(Sleep, 4)),
    Set(
      AssignedStatus(Torment, 4),
      AssignedStatus(Trapped, 3),
      AssignedStatus(Encore, 8)
    )
  )
  val pokemon3 = BattlePokemon(
    Pokemon(
      "Bulbasaur",
      Male,
      Water,
      baseStats = Stats(
        base = Map(
          Stat.Attack -> 49,
          Stat.Defense -> 49,
          Stat.SpecialAttack -> 65,
          Stat.SpecialDefense -> 65,
          Stat.Speed -> 45
        )
      ),
      ability = Ability("Overgrow", Map.empty),
      weightKg = 6.9,
      possibleMoves = List(Move("Vine Whip"), Move("Razor Leaf"))
    ),
    50,
    40,
    List(BattleMove(Move("Vine Whip"), 3)),
    Option(AssignedStatus(Paralyze, 2)),
    Set(
      AssignedStatus(Confusion, 5),
      AssignedStatus(LeechSeed, 6),
      AssignedStatus(Flinch, 1)
    )
  )

  val trainer1 = Trainer("Bob", List(pokemon1), Option(pokemon1))
  val trainer2 = Trainer("Alice", List(pokemon2, pokemon3), Option(pokemon2))

  val mainView: MainView = MainView()
  mainView.setupView()

  val pokemonMap = Map(trainer1 -> pokemon1, trainer2 -> pokemon2)
  val actionBuffer = ActionBuffer(2)

  val battleView = BattleView(mainView.getPlayScreen(), actionBuffer)

  battleView.updatePokemon(pokemonMap)
