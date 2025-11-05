package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color
import com.karchner.Matrix
import com.karchner.Transformation
import com.karchner.Ray
import com.karchner.Sphere
import com.karchner.PointLight
import com.karchner.World
import com.karchner.Intersection
import spock.lang.*

class ShadowTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "There is no shadow when nothing is collinear with point and light"() {
		given:
		def w = World.defaultWorld()
		def p = h.point(0, 10, 0)

		expect:
		!w.is_shadowed(p)
	}

	def "The shadow when an object is between the point and the light"() {
		given:
		def w = World.defaultWorld()
		def p = h.point(10, -10, 10)

		expect:
		w.is_shadowed(p)
	}

	def "There is no shadow when an object is behind the light"() {
		given:
		def w = World.defaultWorld()
		def p = h.point(-20, 20, -20)

		expect:
		!w.is_shadowed(p)
	}

	def "There is no shadow when an object is behind the point"() {
		given:
		def w = World.defaultWorld()
		def p = h.point(-2, 2, -2)

		expect:
		!w.is_shadowed(p)
	}

	def "shade_hit() is given an intersection in shadow"() {
		given:
		def w = new World()
		w.light = new PointLight(h.point(0, 0, -10), new Color(1, 1, 1))
		def s1 = new Sphere()
		w.objects << s1
		def s2 = new Sphere()
		s2.transform = Transformation.translation(0, 0, 10)
		w.objects << s2
		def r = new Ray(h.point(0, 0, 5), h.vector(0, 0, 1))
		def i = new Intersection(4, s2)

		when:
		def comps = World.prepare_computations(i, r)
		def c = w.shade_hit(comps)

		then:
		c == new Color(0.1, 0.1, 0.1)
	}

	def "The hit should offset the point"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def shape = new Sphere()
		shape.transform = Transformation.translation(0, 0, 1)
		def i = new Intersection(5, shape)

		when:
		def comps = World.prepare_computations(i, r)

		then:
		comps.over_point.z < -0.00001 / 2
		comps.point.z > comps.over_point.z
	}
}
