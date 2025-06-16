package it.unibo.skalamon.battle

import it.unibo.skalamon.controller.battle.*
import it.unibo.skalamon.controller.battle.action.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[ActionBuffer]].
  */
class ActionBufferTest extends AnyFlatSpec with should.Matchers:
  val N_TRAINERS: Int = 2

  private val alice: Trainer = Trainer(
    name = "Alice",
    team = List(MutablePokemon("Pickachu"), MutablePokemon("Squirrel"))
  )

  private val bob: Trainer = Trainer(
    name = "Bob",
    team = List(MutablePokemon("Charizard"), MutablePokemon("Wooper"))
  )

  private val gio: Trainer = Trainer(
    name = "Gio",
    team = List(MutablePokemon("Squirrel"), MutablePokemon("Wooper"))
  )

  private val buffer = ActionBuffer(N_TRAINERS)

  "Action buffer" should "allow adding new actions" in:
    val result =
      buffer.register(alice, SwitchAction())
        .register(bob, SwitchAction())
    result.getAction(alice) shouldBe Some(SwitchAction())
    result.getAction(bob) shouldBe Some(SwitchAction())
    result.isFull shouldBe true

  it should "keep its maximum size" in:
    val result =
      buffer.register(alice, SwitchAction())
        .register(bob, SwitchAction())
        .register(gio, SwitchAction())

    result.getAction(alice) shouldBe Some(SwitchAction())
    result.getAction(bob) shouldBe Some(SwitchAction())
    result.getAction(gio) shouldBe None
    result.isFull shouldBe true

  it should "allow withdrawing actions" in:
    val result =
      buffer.register(alice, SwitchAction())
        .register(bob, SwitchAction())
        .withdraw(alice)

    result.getAction(alice) shouldBe None
    result.getAction(bob) shouldBe Some(SwitchAction())
    result.isFull shouldBe false
