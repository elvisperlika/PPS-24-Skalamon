package it.unibo.skalamon.model.status

import it.unibo.skalamon.model.pokemon.{BattlePokemon, PokemonTestUtils}
import it.unibo.skalamon.model.status.volatileStatus.{ProtectEndure, Yawn}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class VolatileStatusTest extends AnyFlatSpec with should.Matchers:

  private val initialTurn = 0
  private val secondTurn = 1

  private val assignedYawn: AssignedStatus[VolatileStatus] =
    AssignedStatus(Yawn(), initialTurn)
  private val assignedProtectEndure: AssignedStatus[VolatileStatus] =
    AssignedStatus(ProtectEndure(), initialTurn)

  "Yawn" should "make the pokemon skip the next turn" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      volatileStatus = Set(assignedYawn)
    )

    pokemon.volatileStatus.head.status.executeEffect(
      pokemon,
      initialTurn,
      initialTurn
    ).skipsCurrentTurn shouldBe false

    pokemon.volatileStatus.head.status.executeEffect(
      pokemon,
      secondTurn,
      initialTurn
    ).skipsCurrentTurn shouldBe true

  "ProtectEndure" should "make the pokemon protected" in:
    val pokemon: BattlePokemon = PokemonTestUtils.simplePokemon4.copy(
      volatileStatus = Set(assignedProtectEndure)
    )
  
    pokemon.volatileStatus.head.status.executeEffect(
      pokemon,
      initialTurn,
      initialTurn
    ).isProtected shouldBe true
