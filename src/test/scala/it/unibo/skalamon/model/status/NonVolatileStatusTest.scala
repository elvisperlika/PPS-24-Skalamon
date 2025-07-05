package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.data.FixedRandomGenerator
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

  "Paralyze" should "make the pokemon skip its turn" in:
    val triggerChance = Paralyze.TriggerChance.asInt
    val valueToSkip = triggerChance - 1
    val valueToNotSkip = triggerChance + 1

    given generatorTrue: FixedRandomGenerator =
      FixedRandomGenerator(valueToSkip)
    given generatorFalse: FixedRandomGenerator =
      FixedRandomGenerator(valueToNotSkip)

    val pokemonTrue: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus =
        Option(AssignedStatus(Paralyze(generatorTrue), initialTurn))
    )
    val pokemonFalse: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus =
        Option(AssignedStatus(Paralyze(generatorFalse), initialTurn))
    )

    pokemonFalse.nonVolatileStatus.get.status.executeEffect(
      pokemonFalse
    ).skipsCurrentTurn shouldBe false

    pokemonTrue.nonVolatileStatus.get.status.executeEffect(
      pokemonTrue
    ).skipsCurrentTurn shouldBe true
