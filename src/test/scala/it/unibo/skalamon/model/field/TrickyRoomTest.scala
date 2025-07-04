package it.unibo.skalamon.model.field

import it.unibo.skalamon.PokemonTestUtils
import it.unibo.skalamon.controller.battle.BattleController
import it.unibo.skalamon.controller.battle.action.MoveAction
import it.unibo.skalamon.model.battle.turn.BattleEvents.{CreateRoom, Hit}
import it.unibo.skalamon.model.battle.{Battle, Trainer}
import it.unibo.skalamon.model.field.room.TrickRoom
import it.unibo.skalamon.model.move.Move
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class TrickyRoomTest extends AnyFlatSpec with should.Matchers:
  // bob Pokémon has 1 hp
  val trainerBob: Trainer = PokemonTestUtils.trainerBob.copy(_inField =
    Some(PokemonTestUtils.trainerBob.team.head)
  )
  val trainerAlice: Trainer = PokemonTestUtils.trainerAlice.copy(_inField =
    Some(PokemonTestUtils.trainerAlice.team.head)
  )

  /** Bob's Pokémon has speed = 45, Alice's Pokémon has speed = 90, same
    * priority -> Alice attack before Bob
    */
  "Tricky room" should "change moves order execution" in:

    val battle: Battle = Battle(trainerBob :: trainerAlice :: Nil)
    val controller: BattleController = BattleController(battle)
    var moves: List[Move] = Nil
    battle.eventManager.watch(Hit) { c =>
      moves = c.origin.move :: moves
    }

    def getTrainerFromState(name: String): Trainer =
      battle.currentTurn.get.state.snapshot.trainers.find(_.name == name).get

    def alice: Trainer = getTrainerFromState("Alice")
    def bob: Trainer = getTrainerFromState("Bob")
    controller.start()
    controller.update()

    val aliceAction = MoveAction(
      move = alice.inField.get.moves.head,
      source = alice,
      target = bob
    )
    val bobAction = MoveAction(
      move = bob.inField.get.moves.head,
      source = bob,
      target = alice
    )

    controller.registerAction(trainerBob, bobAction)
    controller.registerAction(trainerAlice, aliceAction)
    controller.update()
    val predictedMoves =
      bob.inField.get.moves.head.move :: alice.inField.get.moves.head.move :: Nil
    moves shouldBe predictedMoves
    controller.update()
    controller.update()
    controller.update()
    // reset moves
    moves = Nil
    battle.eventManager.notify(CreateRoom of TrickRoom(1))
    controller.registerAction(trainerBob, bobAction)
    controller.registerAction(trainerAlice, aliceAction)
    controller.update()
    moves shouldBe predictedMoves.reverse
