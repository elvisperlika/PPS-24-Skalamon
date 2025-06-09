package it.unibo.skalamon.battle

import it.unibo.skalamon.controller.battle.{
  BattleController,
  BattleControllerImpl,
  MutablePokemon,
  Trainer
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleControllerTest extends AnyFlatSpec with should.Matchers {

  val bcAtStart: BattleController = BattleControllerImpl(
    Trainer(
      "Alice",
      List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
    ),
    Trainer("Bob", List(MutablePokemon("Charizard"), MutablePokemon("Wooper")))
  )

  val bcAtFinish: BattleController = BattleControllerImpl(
    Trainer(
      "Alice",
      List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
    ),
    Trainer(
      "Bob",
      List(MutablePokemon("Charizard", true), MutablePokemon("Wooper", true))
    )
  )

  "Battle controller" should "have 2 trainers" in:
    bcAtStart.trainers shouldBe (
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard"), MutablePokemon("Wooper"))
      )
    )

  it should "end battle when all Pokémon of one player are KO" in:
    bcAtFinish.isOver shouldBe true

  it should "return the winner if battle finished" in:
    bcAtFinish.getWinner shouldBe Some(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
      )
    )

}
