package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color
import com.karchner.Matrix
import com.karchner.Transformation
import com.karchner.Camera
import com.karchner.World
import spock.lang.*

class CameraTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "Constructing a camera"() {
		given:
		def hsize = 160
		def vsize = 120
		def field_of_view = Math.PI/2

		when:
		def c = new Camera(hsize, vsize, field_of_view)

		then:
		c.hsize == 160
		c.vsize == 120
		c.field_of_view == Math.PI/2
		c.transform == Matrix.identity()
	}

	def "The pixel size for a horizontal canvas"() {
		given:
		def c = new Camera(200, 125, Math.PI/2)

		expect:
		Math.abs(c.pixel_size - 0.01) < 0.00001
	}

	def "The pixel size for a vertical canvas"() {
		given:
		def c = new Camera(125, 200, Math.PI/2)

		expect:
		Math.abs(c.pixel_size - 0.01) < 0.00001
	}

	def "Constructing a ray through the center of the canvas"() {
		given:
		def c = new Camera(201, 101, Math.PI/2)

		when:
		def r = c.ray_for_pixel(100, 50)

		then:
		r.origin == h.point(0, 0, 0)
		r.direction == h.vector(0, 0, -1)
	}

	def "Constructing a ray through a corner of the canvas"() {
		given:
		def c = new Camera(201, 101, Math.PI/2)

		when:
		def r = c.ray_for_pixel(0, 0)

		then:
		r.origin == h.point(0, 0, 0)
		r.direction == h.vector(0.66519, 0.33259, -0.66851)
	}

	def "Constructing a ray when the camera is transformed"() {
		given:
		def c = new Camera(201, 101, Math.PI/2)
		c.transform = Transformation.rotation_y(Math.PI/4) * Transformation.translation(0, -2, 5)

		when:
		def r = c.ray_for_pixel(100, 50)

		then:
		r.origin == h.point(0, 2, -5)
		r.direction == h.vector(Math.sqrt(2)/2, 0, -Math.sqrt(2)/2)
	}

	def "Rendering a world with a camera"() {
		given:
		def w = World.defaultWorld()
		def c = new Camera(11, 11, Math.PI/2)
		def from = h.point(0, 0, -5)
		def to = h.point(0, 0, 0)
		def up = h.vector(0, 1, 0)
		c.transform = Camera.view_transform(from, to, up)

		when:
		def image = c.render(w)

		then:
		image.pixel_at(5, 5) == new Color(0.38066, 0.47583, 0.2855)
	}

	def "The transformation matrix for the default orientation"() {
		given:
		def from = h.point(0, 0, 0)
		def to = h.point(0, 0, -1)
		def up = h.vector(0, 1, 0)

		when:
		def t = Camera.view_transform(from, to, up)

		then:
		t == Matrix.identity()
	}

	def "A view transformation matrix looking in positive z direction"() {
		given:
		def from = h.point(0, 0, 0)
		def to = h.point(0, 0, 1)
		def up = h.vector(0, 1, 0)

		when:
		def t = Camera.view_transform(from, to, up)

		then:
		t == Transformation.scaling(-1, 1, -1)
	}

	def "The view transformation moves the world"() {
		given:
		def from = h.point(0, 0, 8)
		def to = h.point(0, 0, 0)
		def up = h.vector(0, 1, 0)

		when:
		def t = Camera.view_transform(from, to, up)

		then:
		t == Transformation.translation(0, 0, -8)
	}

	def "An arbitrary view transformation"() {
		given:
		def from = h.point(1, 3, 2)
		def to = h.point(4, -2, 8)
		def up = h.vector(1, 1, 0)

		when:
		def t = Camera.view_transform(from, to, up)

		then:
		t == new Matrix([
			[-0.50709, 0.50709, 0.67612, -2.36643],
			[0.76772, 0.60609, 0.12122, -2.82843],
			[-0.35857, 0.59761, -0.71714, 0.00000],
			[0.00000, 0.00000, 0.00000, 1.00000]
		])
	}
}
