package com.karchner
import com.karchner.Tuples.RTTuple

class Ray {
	RTTuple origin
	RTTuple direction

	Ray(RTTuple origin, RTTuple direction) {
		this.origin = origin
		this.direction = direction
	}

	RTTuple position(double t) {
		return origin + (direction * t)
	}

	Ray transform(Matrix matrix) {
		return new Ray(matrix * origin, matrix * direction)
	}
}
