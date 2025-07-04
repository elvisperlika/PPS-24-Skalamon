package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.Stat.Attack
import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class NonVolatileStatusTest extends AnyFlatSpec with should.Matchers:

  private val initialTurn: Int = 0

  private val assignedBurn: AssignedStatus[NonVolatileStatus] =
    AssignedStatus(Burn, initialTurn)

  "Burn" should "remove health" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedBurn)
    )

    assignedBurn.status.executeEffect(
      pokemon
    ).currentHP shouldEqual (pokemon.currentHP - pokemon.base.hp / Burn.BurnDamageReduction)

  "Burn" should "halve attack stat" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      nonVolatileStatus = Option(assignedBurn)
    )

    assignedBurn.status.executeEffect(
      pokemon
    ).base.baseStats.base(Attack) shouldEqual
      (pokemon.base.baseStats.base(Attack) / Burn.BurnAttackReduction)
