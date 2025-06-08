package it.unibo.skalamon.battle

import it.unibo.skalamon.controller.battle.{
  BattleController,
  BattleControllerImpl
}
import it.unibo.skalamon.model.trainer.Trainer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BattleControllerTest extends AnyFlatSpec with should.Matchers {

  "Battle controller" should "have 2 trainers" in:
    val t1: Trainer = Trainer("Alice")
    val t2: Trainer = Trainer("Bob")
    val bc: BattleController = new BattleControllerImpl(t1, t2)
    bc.trainers shouldBe (t1, t2)
  

  it should "create a turn" in:
    ???
}
