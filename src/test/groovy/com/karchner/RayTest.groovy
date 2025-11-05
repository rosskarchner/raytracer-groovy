package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Matrix
import com.karchner.Transformation
import com.karchner.Ray
import spock.lang.*

class RayTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "Creating and querying a ray"() {
		given:
		def origin = h.point(1, 2, 3)
		def direction = h.vector(4, 5, 6)

		when:
		def r = new Ray(origin, direction)

		then:
		r.origin == origin
		r.direction == direction
	}

	def "Computing a point from a distance"() {
		given:
		def r = new Ray(h.point(2, 3, 4), h.vector(1, 0, 0))

		expect:
		r.position(0) == h.point(2, 3, 4)
		r.position(1) == h.point(3, 3, 4)
		r.position(-1) == h.point(1, 3, 4)
		r.position(2.5) == h.point(4.5, 3, 4)
	}

	def "Translating a ray"() {
		given:
		def r = new Ray(h.point(1, 2, 3), h.vector(0, 1, 0))
		def m = Transformation.translation(3, 4, 5)

		when:
		def r2 = r.transform(m)

		then:
		r2.origin == h.point(4, 6, 8)
		r2.direction == h.vector(0, 1, 0)
	}

	def "Scaling a ray"() {
		given:
		def r = new Ray(h.point(1, 2, 3), h.vector(0, 1, 0))
		def m = Transformation.scaling(2, 3, 4)

		when:
		def r2 = r.transform(m)

		then:
		r2.origin == h.point(2, 6, 12)
		r2.direction == h.vector(0, 3, 0)
	}
}
