package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Matrix
import com.karchner.Transformation
import com.karchner.Ray
import com.karchner.Sphere
import spock.lang.*

class SphereTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "A ray intersects a sphere at two points"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].t == 4.0
		xs[1].t == 6.0
	}

	def "A ray intersects a sphere at a tangent"() {
		given:
		def r = new Ray(h.point(0, 1, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].t == 5.0
		xs[1].t == 5.0
	}

	def "A ray misses a sphere"() {
		given:
		def r = new Ray(h.point(0, 2, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 0
	}

	def "A ray originates inside a sphere"() {
		given:
		def r = new Ray(h.point(0, 0, 0), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].t == -1.0
		xs[1].t == 1.0
	}

	def "A sphere is behind a ray"() {
		given:
		def r = new Ray(h.point(0, 0, 5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].t == -6.0
		xs[1].t == -4.0
	}

	def "Intersect sets the object on the intersection"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].object == s
		xs[1].object == s
	}

	def "A sphere's default transformation"() {
		given:
		def s = new Sphere()

		expect:
		s.transform == Matrix.identity()
	}

	def "Changing a sphere's transformation"() {
		given:
		def s = new Sphere()
		def t = Transformation.translation(2, 3, 4)

		when:
		s.transform = t

		then:
		s.transform == t
	}

	def "Intersecting a scaled sphere with a ray"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		s.transform = Transformation.scaling(2, 2, 2)
		def xs = s.intersect(r)

		then:
		xs.size() == 2
		xs[0].t == 3
		xs[1].t == 7
	}

	def "Intersecting a translated sphere with a ray"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def s = new Sphere()

		when:
		s.transform = Transformation.translation(5, 0, 0)
		def xs = s.intersect(r)

		then:
		xs.size() == 0
	}

	def "The normal on a sphere at a point on the x axis"() {
		given:
		def s = new Sphere()

		when:
		def n = s.normal_at(h.point(1, 0, 0))

		then:
		n == h.vector(1, 0, 0)
	}

	def "The normal on a sphere at a point on the y axis"() {
		given:
		def s = new Sphere()

		when:
		def n = s.normal_at(h.point(0, 1, 0))

		then:
		n == h.vector(0, 1, 0)
	}

	def "The normal on a sphere at a point on the z axis"() {
		given:
		def s = new Sphere()

		when:
		def n = s.normal_at(h.point(0, 0, 1))

		then:
		n == h.vector(0, 0, 1)
	}

	def "The normal on a sphere at a nonaxial point"() {
		given:
		def s = new Sphere()

		when:
		def n = s.normal_at(h.point(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3))

		then:
		n == h.vector(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3)
	}

	def "The normal is a normalized vector"() {
		given:
		def s = new Sphere()

		when:
		def n = s.normal_at(h.point(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3))

		then:
		n == h.normalize(n)
	}

	def "Computing the normal on a translated sphere"() {
		given:
		def s = new Sphere()
		s.transform = Transformation.translation(0, 1, 0)

		when:
		def n = s.normal_at(h.point(0, 1.70711, -0.70711))

		then:
		n == h.vector(0, 0.70711, -0.70711)
	}

	def "Computing the normal on a transformed sphere"() {
		given:
		def s = new Sphere()
		def m = Transformation.scaling(1, 0.5, 1) * Transformation.rotation_z(Math.PI/5)
		s.transform = m

		when:
		def n = s.normal_at(h.point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2))

		then:
		n == h.vector(0, 0.97014, -0.24254)
	}
}
