package it.unibo.skalamon.model.field.expirable.weather.kind

import it.unibo.skalamon.model.field.expirable.Expirable
import it.unibo.skalamon.model.field.expirable.weather.Weather

trait Rain(t: Int) extends Weather with Expirable
