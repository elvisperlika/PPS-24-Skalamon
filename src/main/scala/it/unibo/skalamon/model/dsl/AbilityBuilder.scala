package it.unibo.skalamon.model.dsl

import it.unibo.skalamon.model.ability.Ability
import it.unibo.skalamon.model.behavior.Behavior
import it.unibo.skalamon.model.event.EventType

/** A builder for creating Ability instances using a DSL-like syntax.
  *
  * @param name
  *   The name of the move being built.
  */
class AbilityBuilder(private val name: String) extends DslBuilder[Ability]:
  private var hooks: Map[EventType[_], Behavior] = Map.empty

  /** Sets the behavior of the ability in case of an event of the specified
    * type.
    *
    * @param eventType
    *   The type of event that triggers the behavior.
    * @return
    *   This for chaining.
    */
  def on(eventType: EventType[_])(behavior: Behavior): AbilityBuilder =
    this.hooks = this.hooks + (eventType -> behavior)
    this

  /** Builds the Ability instance with the provided attributes.
    *
    * @return
    *   The constructed Ability instance.
    */
  override def build: Ability =
    Ability(
      name = name,
      hooks = hooks
    )

/** DSL function to create an Ability instance.
  *
  * @param name
  *   The name of the ability.
  * @param build
  *   A function that takes an `AbilityBuilder` and returns a modified builder.
  * @return
  *   The constructed Ability instance.
  */
def ability(name: String)(build: AbilityBuilder => AbilityBuilder): Ability =
  build(AbilityBuilder(name)).build
