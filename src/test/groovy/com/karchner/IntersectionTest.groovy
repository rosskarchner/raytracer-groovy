package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Ray
import com.karchner.Sphere
import com.karchner.Intersection
import com.karchner.Intersections
import spock.lang.*

class IntersectionTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "An intersection encapsulates t and object"() {
		given:
		def s = new Sphere()

		when:
		def i = new Intersection(3.5, s)

		then:
		i.t == 3.5
		i.object == s
	}

	def "Aggregating intersections"() {
		given:
		def s = new Sphere()
		def i1 = new Intersection(1, s)
		def i2 = new Intersection(2, s)

		when:
		def xs = [i1, i2]

		then:
		xs.size() == 2
		xs[0].t == 1
		xs[1].t == 2
	}

	def "The hit, when all intersections have positive t"() {
		given:
		def s = new Sphere()
		def i1 = new Intersection(1, s)
		def i2 = new Intersection(2, s)
		def xs = [i2, i1]

		when:
		def i = Intersections.hit(xs)

		then:
		i == i1
	}

	def "The hit, when some intersections have negative t"() {
		given:
		def s = new Sphere()
		def i1 = new Intersection(-1, s)
		def i2 = new Intersection(1, s)
		def xs = [i2, i1]

		when:
		def i = Intersections.hit(xs)

		then:
		i == i2
	}

	def "The hit, when all intersections have negative t"() {
		given:
		def s = new Sphere()
		def i1 = new Intersection(-2, s)
		def i2 = new Intersection(-1, s)
		def xs = [i2, i1]

		when:
		def i = Intersections.hit(xs)

		then:
		i == null
	}

	def "The hit is always the lowest nonnegative intersection"() {
		given:
		def s = new Sphere()
		def i1 = new Intersection(5, s)
		def i2 = new Intersection(7, s)
		def i3 = new Intersection(-3, s)
		def i4 = new Intersection(2, s)
		def xs = [i1, i2, i3, i4]

		when:
		def i = Intersections.hit(xs)

		then:
		i == i4
	}
}
