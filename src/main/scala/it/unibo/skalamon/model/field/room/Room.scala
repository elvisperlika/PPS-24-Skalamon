package it.unibo.skalamon.model.field.room

import it.unibo.skalamon.model.field.FieldEffect

/** Like [[Weather]], a [[Room]] effect lasts 5 turns and there can be only one
  * at a time.
  */
trait Room extends FieldEffect
