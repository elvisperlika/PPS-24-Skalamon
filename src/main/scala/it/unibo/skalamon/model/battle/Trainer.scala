package it.unibo.skalamon.model.battle

import it.unibo.skalamon.model.pokemon.BattlePokemon

import java.util.UUID

/** A trainer in a battle.
  *
  * @param name
  *   The name of the trainer.
  * @param team
  *   The list of Pokémon that the trainer has, updated during the battle.
  * @param _inField
  *   The Pokémon currently in the field for this trainer, if any. This is
  *   handled by *reference* to the Pokémon in the team with a matching ID.
  */
case class Trainer(
    name: String,
    team: List[BattlePokemon],
    private val _inField: Option[BattlePokemon] = None,
    id: UUID = UUID.randomUUID()
):
  /** The Pokémon currently in the field for this trainer, if any. */
  def inField: Option[BattlePokemon] =
    _inField.flatMap(inField => team.find(_.id == inField.id))

  /** Returns the team of a trainer without the Pokémon currently in the field.
    * @return
    *   A list of BattlePokemon excluding the one currently in the field.
    */
  def teamWithoutInField: List[BattlePokemon] = inField match
    case Some(pokemon) => team.filterNot(_.id == pokemon.id)
    case None          => team
