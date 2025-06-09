package it.unibo.skalamon.model.pokemon

import it.unibo.skalamon.model.move.*

/** Represents the Stats of a Pokémon.
  *
  * @param hp
  *   The Health Points of a Pokémon.
  * @param attack
  *   The attack of a Pokémon.
  * @param defense
  *   The defense of a Pokémon.
  * @param specialAttack
  *   The specialAttack of a Pokémon.
  * @param specialDefense
  *   The specialDefense of a Pokémon.
  * @param speed
  *   The attack of a Pokémon.
  */
case class Stats(
                  hp: Int,
                  attack: Int,
                  defense: Int,
                  specialAttack: Int,
                  specialDefense: Int,
                  speed: Int
                )

sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Genderless extends Gender

case class Type(name: String) //TODO: to be removed, it is temporary till merge
case class Ability(name: String) //TODO: to be removed, it is temporary till merge

/** Represents the base attributes of a Pokémon.
  * @param name
  *   The name of the Pokémon.
  * @param gender
  *   The gender of the Pokémon.
  * @param types
  *   The list of types of the Pokémon.
  * @param baseStats
  *   The base stats of the Pokémon.
  * @param ability
  *   The ability of the Pokémon.
  * @param weightKg
  *   The weight of the Pokémon in Kg.
  * @param possibleMoves
  *   The list of possible moves of the Pokémon.
 */
case class Pokemon(
                        name: String,
                        gender: Gender,
                        types: List[Type],
                        baseStats: Stats,
                        ability: Ability,
                        weightKg: Double,
                        possibleMoves: List[Move]
                      )

case class BattlePokemon(
                            base: Pokemon,
                            level: Int,
                            currentHP: Int,
                            moves: List[BattleMove]
                          ):
  def actualStats: Stats = calculateStats(base.baseStats, level)

  //TODO: nonvolatilestatus: Option[Status], volitile: List[Status], trait Status, trait nonvolatileStatus, trait volatileStatus
  
  private def calculateStats(base: Stats, level: Int): Stats =
    def scale(stat: Int): Int = (stat * level / 100.0).round.toInt + 5
    
    Stats(
      hp = (base.hp * level / 100.0).round.toInt + level + 10,
      attack = scale(base.attack),
      defense = scale(base.defense),
      specialAttack = scale(base.specialAttack),
      specialDefense = scale(base.specialDefense),
      speed = scale(base.speed)
    )
  
  def isAlive: Boolean = currentHP > 0
  
  def takeDamage(damage: Int): BattlePokemon =
    this.copy(currentHP = math.max(0, currentHP - damage))
