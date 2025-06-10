package it.unibo.skalamon.battle

import it.unibo.skalamon.controller.battle.{
  Action,
  Move,
  MutablePokemon,
  Switch,
  Trainer
}
import it.unibo.skalamon.controller.battle.turn.TurnControllerProxy
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.Predef.Map

class TurnControllerTest extends AnyFlatSpec with should.Matchers:

  val N_TRAINERS: Int = 2
  val tcp: TurnControllerProxy = TurnControllerProxy(N_TRAINERS)
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
    tcp.actions = Map.empty
    tcp.addAction(alice, Switch())
    tcp.actions shouldBe Map[Trainer, Action]((alice, Switch()))

  it should "don't let add more actions than trainers in field" in:
    tcp.actions = Map.empty
    tcp.addAction(alice, Switch())
    tcp.addAction(bob, Move())
    tcp.addAction(gio, Move()) shouldBe a[IllegalArgumentException]
    tcp.actions shouldBe Map[Trainer, Action]((alice, Switch()), (bob, Move()))
