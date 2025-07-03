package it.unibo.skalamon.model.move

import it.unibo.skalamon.PokemonTestUtils
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class HookAllMoveTest extends AnyFlatSpec with should.Matchers:
  
  var trainerBob: Trainer = PokemonTestUtils.trainerBob
  var trainerAlice: Trainer = PokemonTestUtils.trainerAlice
  "Hook all move" should "add " in:
    val battle = Battle(trainerBob :: trainerAlice :: Nil)
    val controller = BattleController(battle)
