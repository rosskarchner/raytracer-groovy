package com.karchner
import com.karchner.Tuples.RTTuple
import com.karchner.Canvas.Color

class PointLight {
	RTTuple position
	Color intensity

	PointLight(RTTuple position, Color intensity) {
		this.position = position
		this.intensity = intensity
	}
}
