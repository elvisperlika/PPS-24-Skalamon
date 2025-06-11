package it.unibo.skalamon.battle

import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import it.unibo.skalamon.controller.battle.*
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.Predef.Map

class TurnControllerTest
    extends AnyFlatSpec
    with should.Matchers
    with ScalaFutures:

  val N_TRAINERS: Int = 2
  val alice: Trainer = Trainer(
    name = "Alice",
    team = List(MutablePokemon("Pickachu"), MutablePokemon("Squirrel"))
  )
  val bob: Trainer = Trainer(
    name = "Bob",
    team = List(MutablePokemon("Charizard"), MutablePokemon("Wooper"))
  )
  val gio: Trainer = Trainer(
    name = "Gio",
    team = List(MutablePokemon("Squirrel"), MutablePokemon("Wooper"))
  )

  "TCP" should "let add new actions" in:
    val tcp: TurnControllerProxy = TurnControllerProxy(N_TRAINERS)
    tcp.addAction(alice, Switch())
    tcp.actions shouldBe Map[Trainer, Action]((alice, Switch()))

  it should "don't let add more actions than trainers in field" in:
    val tcp: TurnControllerProxy = TurnControllerProxy(N_TRAINERS)
    tcp.addAction(alice, Switch())
    tcp.addAction(bob, Move())
    assertThrows[IllegalArgumentException] {
      tcp.addAction(gio, Move())
    }
    tcp.actions shouldBe Map[Trainer, Action]((alice, Switch()), (bob, Move()))

  it should "return actions when ready" in:
    val tcp: TurnControllerProxy = TurnControllerProxy(N_TRAINERS)
    tcp.addAction(alice, Switch())
    tcp.addAction(bob, Move())
    whenReady(tcp.getChosenActions) { result =>
      result shouldBe Map[Trainer, Action]((alice, Switch()), (bob, Move()))
    }

  it should "have not been completed when not all users made own choice" in:
    val tcp: TurnControllerProxy = TurnControllerProxy(N_TRAINERS)
    tcp.addAction(alice, Switch())
    tcp.getChosenActions.isCompleted shouldBe false
