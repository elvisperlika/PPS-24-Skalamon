package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.EmptyBehavior
import it.unibo.skalamon.model.behavior.kind.SingleHitBehavior
import it.unibo.skalamon.model.event.TurnStageEvents
import it.unibo.skalamon.model.move.MoveModel.Accuracy
import it.unibo.skalamon.model.move.MoveModel.Category.Physical
import it.unibo.skalamon.model.pokemon.Stat
import it.unibo.skalamon.model.types.TypesCollection.{Electric, Fire, Flying}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/** Tests for building Pokémon via DSL. */
class PokemonDslTest extends AnyFlatSpec with should.Matchers:
  private val emptyAbility = Ability("No Ability", Map.empty)
  private val baseHp = 100

  "Pokemon DSL" should "create with one type" in:
    val pikachu = pokemon("Pikachu"):
      _ typed Electric hp baseHp weighing 6.0.kg ability emptyAbility

    pikachu.name shouldBe "Pikachu"
    pikachu.types shouldBe Electric :: Nil

  it should "allow combining types" in:
    val charizard = pokemon("Charizard"):
      _ typed Fire + Flying hp baseHp weighing 90.5.kg ability emptyAbility

    charizard.types shouldBe Fire :: Flying :: Nil

  it should "throw an exception if types are not defined" in:
    an[IllegalArgumentException] should be thrownBy:
      pokemon("Unknown")(identity)

  it should "allow setting HP" in:
    val bulbasaur = pokemon("Bulbasaur"):
      _ typed Electric hp baseHp weighing 6.0.kg ability emptyAbility

    bulbasaur.hp shouldBe baseHp
  
  it should "allow setting stats" in:
    val bulbasaur = pokemon("Pikachu"):
      _ typed Electric hp baseHp weighing 6.0.kg stat Stat.Attack -> 20 stat Stat.Defense -> 10 ability emptyAbility

    bulbasaur.stats.base shouldBe Map(
      Stat.Attack -> 20,
      Stat.Defense -> 10
    )
    
  it should "allow setting ability" in:
    val pikachu = pokemon("Pikachu"):
      _ typed Electric hp baseHp weighing 6.0.kg ability ability("Static")(_.on(TurnStageEvents.Started)(EmptyBehavior))

    pikachu.ability.name shouldBe "Static"
    
  it should "allow setting moves" in:
    val pikachu = pokemon("Pikachu"):
      _ typed Electric hp baseHp weighing 6.0.kg ability emptyAbility moves
          move("Thunder Shock", Electric, Physical)(_.neverFailing onSuccess SingleHitBehavior(40))

    pikachu.moves should have size 1
    pikachu.moves.head.name shouldBe "Thunder Shock"
    pikachu.moves.head.accuracy shouldBe Accuracy.NeverFail
