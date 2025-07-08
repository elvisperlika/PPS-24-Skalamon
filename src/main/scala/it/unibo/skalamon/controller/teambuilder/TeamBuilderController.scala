package it.unibo.skalamon.controller.teambuilder

import it.unibo.skalamon.model.battle.Trainer
import it.unibo.skalamon.model.pokemon.{Male, Pokemon}

/** Controller for building a team of Pokémon for a trainer.
  */
trait TeamBuilderController:
  /** @return
    *   A list of all Pokémon from the Pokémon factory.
    */
  def allPokemon: List[Pokemon]

  /** @return
    *   All Pokémon (see [[allPokemon]]) where each Pokémon is mapped to a
    *   unique character that can be used for interaction.
    */
  def pokemonDictionary: Map[Char, Pokemon] =
    allPokemon
      .zipWithIndex
      .map { case (pokemon, index) => (('A' + index).toChar, pokemon) }
      .toMap

  /** Builds a trainer with a team of Pokémon based on a sequence of characters,
    * where each character corresponds to a Pokémon in [[pokemonDictionary]].
    *
    * @param name
    *   The name of the trainer.
    * @param sequence
    *   A string where each character represents a Pokémon in the dictionary, case-insensitive.
    * @return
    *   A new trainer with the specified name and a team of Pokémon
    * @throws IllegalArgumentException
    *   If the sequence exceeds the maximum team size or contains an invalid
    *   character.
    */
  def buildTrainer(name: String, sequence: String): Trainer

object TeamBuilderController:
  /** Creates a new instance of the team builder controller.
    *
    * @return
    *   A new instance of the team builder controller.
    */
  def apply(): TeamBuilderController = new TeamBuilderControllerImpl

private class TeamBuilderControllerImpl extends TeamBuilderController:
  private val MaxTeamSize = 6

  override def allPokemon: List[Pokemon] =
    val cls = Pokemon.getClass
    cls.getDeclaredMethods
      .filter(_.getParameterCount == 0)
      .filter(_.getReturnType == classOf[Pokemon])
      .map(_.invoke(Pokemon).asInstanceOf[Pokemon])
      .toList

  override def buildTrainer(name: String, sequence: String): Trainer =
    import it.unibo.skalamon.model.dsl.battling

    if (sequence.length > MaxTeamSize) then
      throw new IllegalArgumentException(
        s"Team size exceeds maximum of $MaxTeamSize Pokémon."
      )

    val team = sequence.toUpperCase.toList.map: char =>
      pokemonDictionary.getOrElse(
        char,
        throw new IllegalArgumentException(s"Invalid character: $char")
      )

    Trainer(name, team.map(_.battling(Male)))
