package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Matrix
import com.karchner.Transformation
import spock.lang.*

class TransformationTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "Multiplying by a translation matrix"() {
		given:
		def transform = Transformation.translation(5, -3, 2)
		def p = h.point(-3, 4, 5)

		expect:
		transform * p == h.point(2, 1, 7)
	}

	def "Multiplying by the inverse of a translation matrix"() {
		given:
		def transform = Transformation.translation(5, -3, 2)
		def inv = transform.inverse()
		def p = h.point(-3, 4, 5)

		expect:
		inv * p == h.point(-8, 7, 3)
	}

	def "Translation does not affect vectors"() {
		given:
		def transform = Transformation.translation(5, -3, 2)
		def v = h.vector(-3, 4, 5)

		expect:
		transform * v == v
	}

	def "A scaling matrix applied to a point"() {
		given:
		def transform = Transformation.scaling(2, 3, 4)
		def p = h.point(-4, 6, 8)

		expect:
		transform * p == h.point(-8, 18, 32)
	}

	def "A scaling matrix applied to a vector"() {
		given:
		def transform = Transformation.scaling(2, 3, 4)
		def v = h.vector(-4, 6, 8)

		expect:
		transform * v == h.vector(-8, 18, 32)
	}

	def "Multiplying by the inverse of a scaling matrix"() {
		given:
		def transform = Transformation.scaling(2, 3, 4)
		def inv = transform.inverse()
		def v = h.vector(-4, 6, 8)

		expect:
		inv * v == h.vector(-2, 2, 2)
	}

	def "Reflection is scaling by a negative value"() {
		given:
		def transform = Transformation.scaling(-1, 1, 1)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(-2, 3, 4)
	}

	def "Rotating a point around the x axis"() {
		given:
		def p = h.point(0, 1, 0)
		def half_quarter = Transformation.rotation_x(Math.PI / 4)
		def full_quarter = Transformation.rotation_x(Math.PI / 2)

		expect:
		half_quarter * p == h.point(0, Math.sqrt(2)/2, Math.sqrt(2)/2)
		full_quarter * p == h.point(0, 0, 1)
	}

	def "The inverse of an x-rotation rotates in the opposite direction"() {
		given:
		def p = h.point(0, 1, 0)
		def half_quarter = Transformation.rotation_x(Math.PI / 4)
		def inv = half_quarter.inverse()

		expect:
		inv * p == h.point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2)
	}

	def "Rotating a point around the y axis"() {
		given:
		def p = h.point(0, 0, 1)
		def half_quarter = Transformation.rotation_y(Math.PI / 4)
		def full_quarter = Transformation.rotation_y(Math.PI / 2)

		expect:
		half_quarter * p == h.point(Math.sqrt(2)/2, 0, Math.sqrt(2)/2)
		full_quarter * p == h.point(1, 0, 0)
	}

	def "Rotating a point around the z axis"() {
		given:
		def p = h.point(0, 1, 0)
		def half_quarter = Transformation.rotation_z(Math.PI / 4)
		def full_quarter = Transformation.rotation_z(Math.PI / 2)

		expect:
		half_quarter * p == h.point(-Math.sqrt(2)/2, Math.sqrt(2)/2, 0)
		full_quarter * p == h.point(-1, 0, 0)
	}

	def "A shearing transformation moves x in proportion to y"() {
		given:
		def transform = Transformation.shearing(1, 0, 0, 0, 0, 0)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(5, 3, 4)
	}

	def "A shearing transformation moves x in proportion to z"() {
		given:
		def transform = Transformation.shearing(0, 1, 0, 0, 0, 0)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(6, 3, 4)
	}

	def "A shearing transformation moves y in proportion to x"() {
		given:
		def transform = Transformation.shearing(0, 0, 1, 0, 0, 0)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(2, 5, 4)
	}

	def "A shearing transformation moves y in proportion to z"() {
		given:
		def transform = Transformation.shearing(0, 0, 0, 1, 0, 0)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(2, 7, 4)
	}

	def "A shearing transformation moves z in proportion to x"() {
		given:
		def transform = Transformation.shearing(0, 0, 0, 0, 1, 0)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(2, 3, 6)
	}

	def "A shearing transformation moves z in proportion to y"() {
		given:
		def transform = Transformation.shearing(0, 0, 0, 0, 0, 1)
		def p = h.point(2, 3, 4)

		expect:
		transform * p == h.point(2, 3, 7)
	}

	def "Individual transformations are applied in sequence"() {
		given:
		def p = h.point(1, 0, 1)
		def A = Transformation.rotation_x(Math.PI / 2)
		def B = Transformation.scaling(5, 5, 5)
		def C = Transformation.translation(10, 5, 7)

		when:
		def p2 = A * p
		def p3 = B * p2
		def p4 = C * p3

		then:
		p2 == h.point(1, -1, 0)
		p3 == h.point(5, -5, 0)
		p4 == h.point(15, 0, 7)
	}

	def "Chained transformations must be applied in reverse order"() {
		given:
		def p = h.point(1, 0, 1)
		def A = Transformation.rotation_x(Math.PI / 2)
		def B = Transformation.scaling(5, 5, 5)
		def C = Transformation.translation(10, 5, 7)

		when:
		def T = C * B * A

		then:
		T * p == h.point(15, 0, 7)
	}
}
