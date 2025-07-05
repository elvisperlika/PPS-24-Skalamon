package it.unibo.skalamon.model.dsl

/** A trait for building DSL constructs.
  *
  * @tparam T
  *   The type of construct being built.
  */
trait DslBuilder[T]:
  /** Builds the construct with the provided attributes.
    *
    * @return
    *   The constructed instance of type T.
    */
  def build: T
