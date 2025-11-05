package com.karchner
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color

class World {
	List<Sphere> objects
	PointLight light
	private static TupleHelpers h = new TupleHelpers()

	World() {
		this.objects = []
		this.light = null
	}

	static World defaultWorld() {
		def helper = new TupleHelpers()
		def w = new World()
		w.light = new PointLight(helper.point(-10, 10, -10), new Color(1, 1, 1))

		def s1 = new Sphere()
		s1.material.color = new Color(0.8, 1.0, 0.6)
		s1.material.diffuse = 0.7
		s1.material.specular = 0.2

		def s2 = new Sphere()
		s2.transform = Transformation.scaling(0.5, 0.5, 0.5)

		w.objects = [s1, s2]
		return w
	}

	List<Intersection> intersect_world(Ray ray) {
		def intersections = []
		for (obj in objects) {
			intersections.addAll(obj.intersect(ray))
		}
		return intersections.sort()
	}

	static Map prepare_computations(Intersection intersection, Ray ray) {
		def helper = new TupleHelpers()
		def comps = [:]
		comps.t = intersection.t
		comps.object = intersection.object
		comps.point = ray.position(intersection.t)
		comps.eyev = ray.direction.negative()
		comps.normalv = intersection.object.normal_at(comps.point)

		if (helper.dot(comps.normalv, comps.eyev) < 0) {
			comps.inside = true
			comps.normalv = comps.normalv.negative()
		} else {
			comps.inside = false
		}

		// Offset the point slightly in the direction of the normal
		// to prevent self-shadowing
		comps.over_point = comps.point + comps.normalv * 0.00001

		return comps
	}

	Color shade_hit(Map comps) {
		def shadowed = is_shadowed(comps.over_point)
		return Lighting.lighting(comps.object.material, light, comps.point,
		                         comps.eyev, comps.normalv, shadowed)
	}

	Color color_at(Ray ray) {
		def intersections = intersect_world(ray)
		def hit = Intersections.hit(intersections)

		if (hit == null) {
			return new Color(0, 0, 0)
		}

		def comps = prepare_computations(hit, ray)
		return shade_hit(comps)
	}

	boolean is_shadowed(RTTuple point) {
		def helper = new TupleHelpers()
		def v = light.position - point
		def distance = helper.magnitude(v)
		def direction = helper.normalize(v)

		def r = new Ray(point, direction)
		def intersections = intersect_world(r)

		def h_intersection = Intersections.hit(intersections)
		if (h_intersection != null && h_intersection.t < distance) {
			return true
		}

		return false
	}
}
