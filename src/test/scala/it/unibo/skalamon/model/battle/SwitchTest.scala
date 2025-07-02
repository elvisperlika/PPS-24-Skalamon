package it.unibo.skalamon.model.battle

import it.unibo.skalamon.PokemonTestUtils
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.controller.battle.action.SwitchAction
import it.unibo.skalamon.model.dsl.battling
import it.unibo.skalamon.model.pokemon.{Male, Pokemon}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SwitchTest extends AnyFlatSpec with should.Matchers:

  "Switch" should "change trainer's Pokémon in field" in:
    var trainerBob = PokemonTestUtils.trainerBob
    val p1 = Pokemon.machamp.battling(Male)
    val p2 = Pokemon.alakazam.battling(Male)
    val team = p1 :: p2 :: Nil
    trainerBob =
      trainerBob.copy(team = team, _inField = None)
    val battle = Battle(trainerBob :: Nil)
    val controller = BattleController(battle)
    controller.start()
    controller.update()

    val bobAction = SwitchAction(pIn = trainerBob.team.find(_.id == p1.id).get)
    battle.currentTurn.get.state.snapshot.trainers.head.inField shouldBe None

    controller.registerAction(trainerBob, bobAction)
    controller.update()
    battle.currentTurn.get.state.snapshot.trainers.head.inField shouldBe Some(
      p1
    )
