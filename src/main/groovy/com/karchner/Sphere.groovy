package com.karchner
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import java.lang.Math

class Sphere {
	Matrix transform
	Material material
	private static TupleHelpers h = new TupleHelpers()

	Sphere() {
		this.transform = Matrix.identity()
		this.material = new Material()
	}

	List<Intersection> intersect(Ray ray) {
		def helper = new TupleHelpers()
		// Transform the ray by the inverse of the sphere's transformation
		def ray2 = ray.transform(transform.inverse())

		// Vector from sphere center (0,0,0) to ray origin
		def sphere_to_ray = ray2.origin - helper.point(0, 0, 0)

		def a = helper.dot(ray2.direction, ray2.direction)
		def b = 2 * helper.dot(ray2.direction, sphere_to_ray)
		def c = helper.dot(sphere_to_ray, sphere_to_ray) - 1

		def discriminant = b * b - 4 * a * c

		if (discriminant < 0) {
			return []
		}

		def t1 = (-b - Math.sqrt(discriminant)) / (2 * a)
		def t2 = (-b + Math.sqrt(discriminant)) / (2 * a)

		return [new Intersection(t1, this), new Intersection(t2, this)]
	}

	RTTuple normal_at(RTTuple world_point) {
		def helper = new TupleHelpers()
		def object_point = transform.inverse() * world_point
		def object_normal = object_point - helper.point(0, 0, 0)
		def world_normal = transform.inverse().transpose() * object_normal
		// Set w to 0 to make it a vector
		world_normal = helper.vector(world_normal.x, world_normal.y, world_normal.z)
		return helper.normalize(world_normal)
	}
}
