package com.karchner

class Intersection implements Comparable<Intersection> {
	double t
	Object object

	Intersection(double t, Object object) {
		this.t = t
		this.object = object
	}

	int compareTo(Intersection other) {
		return Double.compare(this.t, other.t)
	}

	boolean equals(Object other) {
		if (!(other instanceof Intersection)) {
			return false
		}
		Intersection i = (Intersection) other
		return this.t == i.t && this.object == i.object
	}
}

class Intersections {
	static Intersection hit(List<Intersection> intersections) {
		def validHits = intersections.findAll { it.t >= 0 }
		if (validHits.isEmpty()) {
			return null
		}
		return validHits.min { it.t }
	}
}
