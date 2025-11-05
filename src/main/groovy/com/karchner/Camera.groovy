package com.karchner
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Canvas
import java.lang.Math

class Camera {
	int hsize
	int vsize
	double field_of_view
	Matrix transform
	double pixel_size
	double half_width
	double half_height

	private static TupleHelpers h = new TupleHelpers()

	Camera(int hsize, int vsize, double field_of_view) {
		this.hsize = hsize
		this.vsize = vsize
		this.field_of_view = field_of_view
		this.transform = Matrix.identity()

		def half_view = Math.tan(field_of_view / 2)
		def aspect = (double) hsize / vsize

		if (aspect >= 1) {
			this.half_width = half_view
			this.half_height = half_view / aspect
		} else {
			this.half_width = half_view * aspect
			this.half_height = half_view
		}

		this.pixel_size = (half_width * 2) / hsize
	}

	Ray ray_for_pixel(int px, int py) {
		def helper = new TupleHelpers()
		// The offset from the edge of the canvas to the pixel's center
		def xoffset = (px + 0.5) * pixel_size
		def yoffset = (py + 0.5) * pixel_size

		// The untransformed coordinates of the pixel in world space
		// (camera looks toward -z, so +x is to the *left*)
		def world_x = half_width - xoffset
		def world_y = half_height - yoffset

		// Using the camera matrix, transform the canvas point and the origin,
		// and then compute the ray's direction vector
		// (canvas is at z=-1)
		def inv = transform.inverse()
		def pixel = inv * helper.point(world_x, world_y, -1)
		def origin = inv * helper.point(0, 0, 0)
		def direction = helper.normalize(pixel - origin)

		return new Ray(origin, direction)
	}

	Canvas render(World world) {
		def image = new Canvas(hsize, vsize)

		for (int y = 0; y < vsize; y++) {
			for (int x = 0; x < hsize; x++) {
				def ray = ray_for_pixel(x, y)
				def color = world.color_at(ray)
				image.write_pixel(x, y, color)
			}
		}

		return image
	}

	static Matrix view_transform(RTTuple from, RTTuple to, RTTuple up) {
		def helper = new TupleHelpers()
		def forward = helper.normalize(to - from)
		def left = helper.cross(forward, helper.normalize(up))
		def true_up = helper.cross(left, forward)

		def orientation = new Matrix([
			[left.x, left.y, left.z, 0],
			[true_up.x, true_up.y, true_up.z, 0],
			[-forward.x, -forward.y, -forward.z, 0],
			[0, 0, 0, 1]
		])

		return orientation * Transformation.translation(-from.x, -from.y, -from.z)
	}
}
