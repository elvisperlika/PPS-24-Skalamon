package it.unibo.skalamon.model.field.weather.kind

import it.unibo.skalamon.model.field.Expirable
import it.unibo.skalamon.model.field.weather.Weather

trait Rain(t: Int) extends Weather with Expirable
