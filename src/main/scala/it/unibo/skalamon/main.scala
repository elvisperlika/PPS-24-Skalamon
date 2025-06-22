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

  val trainer1 = Trainer("Bob", List.empty)
  val trainer2 = Trainer("Alice", List(pokemon2), Option(pokemon2))

  val mainView: MainView = MainView()
  mainView.setupView()

  val pokemonMap = Map(trainer1 -> pokemon1, trainer2 -> pokemon2)
  val actionBuffer = ActionBuffer(2)

  val battleView = BattleView(mainView.getPlayScreen(), actionBuffer)

  battleView.updatePokemon(pokemonMap)
