package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.Stat.*
import it.unibo.skalamon.model.status.nonVolatileStatus.*
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class NonVolatileStatusTest extends AnyFlatSpec with should.Matchers:

  private val initialTurn: Int = 0

  private val assignedBurn: AssignedStatus[NonVolatileStatus] =
    AssignedStatus(Burn(), initialTurn)
  private val assignedParalyze: AssignedStatus[NonVolatileStatus] =
    AssignedStatus(Paralyze(), initialTurn)

  "NonVolatileStatus" should "make the pokemon skip its turn" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedParalyze)
    )

    pokemon.nonVolatileStatus.get.status.skipTurn(
      pokemon,
      0
    ).skipsCurrentTurn shouldBe false

    pokemon.nonVolatileStatus.get.status.skipTurn(
      pokemon,
      100
    ).skipsCurrentTurn shouldBe true

  "Burn" should "remove health" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedBurn)
    )

    assignedBurn.status.executeEffect(
      pokemon
    ).currentHP shouldEqual (pokemon.currentHP - pokemon.base.hp / Burn.DamageReduction)

  "Burn" should "halve attack stat" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedBurn)
    )

    assignedBurn.status.executeEffect(
      pokemon
    ).base.baseStats.base(Attack) shouldEqual
      (pokemon.base.baseStats.base(Attack) / Burn.AttackReduction)

  "Paralyze" should "halve speed stat" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedParalyze)
    )

    assignedParalyze.status.executeEffect(
      pokemon
    ).base.baseStats.base(Speed) shouldEqual
      (pokemon.base.baseStats.base(
        Speed
      ) / Paralyze.AttackReduction)
