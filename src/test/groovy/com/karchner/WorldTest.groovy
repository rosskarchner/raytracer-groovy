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
import com.karchner.Intersections
import spock.lang.*

class WorldTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "Creating a world"() {
		given:
		def w = new World()

		expect:
		w.objects.isEmpty()
		w.light == null
	}

	def "The default world"() {
		given:
		def light = new PointLight(h.point(-10, 10, -10), new Color(1, 1, 1))
		def s1 = new Sphere()
		s1.material.color = new Color(0.8, 1.0, 0.6)
		s1.material.diffuse = 0.7
		s1.material.specular = 0.2

		def s2 = new Sphere()
		s2.transform = Transformation.scaling(0.5, 0.5, 0.5)

		when:
		def w = World.defaultWorld()

		then:
		w.light == light
		w.objects.contains(s1)
		w.objects.contains(s2)
	}

	def "Intersect a world with a ray"() {
		given:
		def w = World.defaultWorld()
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))

		when:
		def xs = w.intersect_world(r)

		then:
		xs.size() == 4
		xs[0].t == 4
		xs[1].t == 4.5
		xs[2].t == 5.5
		xs[3].t == 6
	}

	def "Precomputing the state of an intersection"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def shape = new Sphere()
		def i = new Intersection(4, shape)

		when:
		def comps = World.prepare_computations(i, r)

		then:
		comps.t == i.t
		comps.object == i.object
		comps.point == h.point(0, 0, -1)
		comps.eyev == h.vector(0, 0, -1)
		comps.normalv == h.vector(0, 0, -1)
	}

	def "The hit, when an intersection occurs on the outside"() {
		given:
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def shape = new Sphere()
		def i = new Intersection(4, shape)

		when:
		def comps = World.prepare_computations(i, r)

		then:
		!comps.inside
	}

	def "The hit, when an intersection occurs on the inside"() {
		given:
		def r = new Ray(h.point(0, 0, 0), h.vector(0, 0, 1))
		def shape = new Sphere()
		def i = new Intersection(1, shape)

		when:
		def comps = World.prepare_computations(i, r)

		then:
		comps.point == h.point(0, 0, 1)
		comps.eyev == h.vector(0, 0, -1)
		comps.inside
		comps.normalv == h.vector(0, 0, -1)
	}

	def "Shading an intersection"() {
		given:
		def w = World.defaultWorld()
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))
		def shape = w.objects[0]
		def i = new Intersection(4, shape)

		when:
		def comps = World.prepare_computations(i, r)
		def c = w.shade_hit(comps)

		then:
		c == new Color(0.38066, 0.47583, 0.2855)
	}

	def "Shading an intersection from the inside"() {
		given:
		def w = World.defaultWorld()
		w.light = new PointLight(h.point(0, 0.25, 0), new Color(1, 1, 1))
		def r = new Ray(h.point(0, 0, 0), h.vector(0, 0, 1))
		def shape = w.objects[1]
		def i = new Intersection(0.5, shape)

		when:
		def comps = World.prepare_computations(i, r)
		def c = w.shade_hit(comps)

		then:
		c == new Color(0.90498, 0.90498, 0.90498)
	}

	def "The color when a ray misses"() {
		given:
		def w = World.defaultWorld()
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 1, 0))

		when:
		def c = w.color_at(r)

		then:
		c == new Color(0, 0, 0)
	}

	def "The color when a ray hits"() {
		given:
		def w = World.defaultWorld()
		def r = new Ray(h.point(0, 0, -5), h.vector(0, 0, 1))

		when:
		def c = w.color_at(r)

		then:
		c == new Color(0.38066, 0.47583, 0.2855)
	}

	def "The color with an intersection behind the ray"() {
		given:
		def w = World.defaultWorld()
		def outer = w.objects[0]
		outer.material.ambient = 1
		def inner = w.objects[1]
		inner.material.ambient = 1
		def r = new Ray(h.point(0, 0, 0.75), h.vector(0, 0, -1))

		when:
		def c = w.color_at(r)

		then:
		c == inner.material.color
	}
}
