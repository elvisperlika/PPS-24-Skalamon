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
    List(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard"), MutablePokemon("Wooper"))
      )
    )
  )

  val bcAtFinish: BattleController = BattleControllerImpl(
    List(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu", true), MutablePokemon("Squirell"))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard", true), MutablePokemon("Wooper", true))
      ),
      Trainer(
        "Jack",
        List(MutablePokemon("Girafarig", true), MutablePokemon("Drampa", true))
      )
    )
  )

  val bcOnBattle: BattleController = BattleControllerImpl(
    List(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu", true), MutablePokemon("Squirell"))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard", true), MutablePokemon("Wooper", true))
      ),
      Trainer(
        "Jack",
        List(MutablePokemon("Girafarig", false), MutablePokemon("Drampa", false))
      )
    )
  )

  val bcWithoutWinners: BattleController = BattleControllerImpl(
    List(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu", true), MutablePokemon("Squirell", true))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard", true), MutablePokemon("Wooper", true))
      ),
      Trainer(
        "Jack",
        List(MutablePokemon("Girafarig", true), MutablePokemon("Drampa", true))
      )
    )
  )

  "Battle controller" should "have trainers list" in:
    bcAtStart.trainers shouldBe List(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
      ),
      Trainer(
        "Bob",
        List(MutablePokemon("Charizard"), MutablePokemon("Wooper"))
      )
    )

  it should "end battle when all teams except one are KO" in:
    bcAtFinish.isOver shouldBe true

  it should "not end battle when there are more than one team 'alive'" in:
    bcOnBattle.isOver shouldBe false

  it should "be on draw if all teams ar KO" in:
    bcWithoutWinners.isDraw shouldBe true

  it should "not be on draw if a team is alive" in:
    bcAtFinish.isDraw shouldBe false

  it should "return the winner if battle finished" in:
    bcAtFinish.getWinner shouldBe Some(
      Trainer(
        "Alice",
        List(MutablePokemon("Pikachu"), MutablePokemon("Squirell"))
      )
    )

}
