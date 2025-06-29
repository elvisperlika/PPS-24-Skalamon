package it.unibo.skalamon.controller.battle

import it.unibo.skalamon.controller.battle.action.*
import it.unibo.skalamon.model.pokemon.PokemonTestUtils
import it.unibo.skalamon.model.pokemon.PokemonTestUtils.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for [[ActionBuffer]].
  */
class ActionBufferTest extends AnyFlatSpec with should.Matchers:
  val N_TRAINERS: Int = 2

  private val alice = trainerAlice
  private val bob = trainerBob
  private val gio = trainerGio

  private val buffer = ActionBuffer(N_TRAINERS)

  "Action buffer" should "allow adding new actions" in:
    val result =
      buffer.register(alice, SwitchAction(simplePokemon1, simplePokemon2))
        .register(bob, SwitchAction(simplePokemon1, simplePokemon2))

    result.getAction(alice) shouldBe Some(SwitchAction(simplePokemon1, simplePokemon2))
    result.getAction(bob) shouldBe Some(SwitchAction(simplePokemon1, simplePokemon2))
    result.isFull shouldBe true

  it should "keep its maximum size" in:
    val result =
      buffer.register(alice, SwitchAction(simplePokemon1, simplePokemon2))
        .register(bob, SwitchAction(simplePokemon1, simplePokemon2))
        .register(gio, SwitchAction(simplePokemon1, simplePokemon2))

    result.getAction(alice) shouldBe Some(SwitchAction(simplePokemon1, simplePokemon2))
    result.getAction(bob) shouldBe Some(SwitchAction(simplePokemon1, simplePokemon2))
    result.getAction(gio) shouldBe None
    result.isFull shouldBe true

  it should "allow withdrawing actions" in:
    val result =
      buffer.register(alice, SwitchAction(simplePokemon1, simplePokemon2))
        .register(bob, SwitchAction(simplePokemon1, simplePokemon2))
        .withdraw(alice)

    result.getAction(alice) shouldBe None
    result.getAction(bob) shouldBe Some(SwitchAction(simplePokemon1, simplePokemon2))
    result.isFull shouldBe false

  it should "allow clearing all actions" in:
    val result =
      buffer.register(alice, SwitchAction(simplePokemon1, simplePokemon2))
        .register(bob, SwitchAction(simplePokemon1, simplePokemon2))
        .clear()

    result.getAction(alice) shouldBe None
    result.getAction(bob) shouldBe None
    result.isFull shouldBe false
