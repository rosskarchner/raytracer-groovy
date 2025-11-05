package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color
import com.karchner.Material
import com.karchner.PointLight
import com.karchner.Lighting
import com.karchner.Sphere
import spock.lang.*

class LightingTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "The default material"() {
		given:
		def m = new Material()

		expect:
		m.color == new Color(1, 1, 1)
		m.ambient == 0.1
		m.diffuse == 0.9
		m.specular == 0.9
		m.shininess == 200.0
	}

	def "A sphere has a default material"() {
		given:
		def s = new Sphere()

		expect:
		s.material == new Material()
	}

	def "A sphere may be assigned a material"() {
		given:
		def s = new Sphere()
		def m = new Material()
		m.ambient = 1

		when:
		s.material = m

		then:
		s.material == m
	}

	def "A point light has a position and intensity"() {
		given:
		def intensity = new Color(1, 1, 1)
		def position = h.point(0, 0, 0)

		when:
		def light = new PointLight(position, intensity)

		then:
		light.position == position
		light.intensity == intensity
	}

	def "Lighting with the eye between the light and the surface"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, 0, -1)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 0, -10), new Color(1, 1, 1))

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, false)

		then:
		result == new Color(1.9, 1.9, 1.9)
	}

	def "Lighting with the eye between light and surface, eye offset 45°"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, Math.sqrt(2)/2, -Math.sqrt(2)/2)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 0, -10), new Color(1, 1, 1))

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, false)

		then:
		result == new Color(1.0, 1.0, 1.0)
	}

	def "Lighting with eye opposite surface, light offset 45°"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, 0, -1)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 10, -10), new Color(1, 1, 1))

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, false)

		then:
		result == new Color(0.7364, 0.7364, 0.7364)
	}

	def "Lighting with eye in the path of the reflection vector"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, -Math.sqrt(2)/2, -Math.sqrt(2)/2)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 10, -10), new Color(1, 1, 1))

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, false)

		then:
		result == new Color(1.6364, 1.6364, 1.6364)
	}

	def "Lighting with the light behind the surface"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, 0, -1)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 0, 10), new Color(1, 1, 1))

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, false)

		then:
		result == new Color(0.1, 0.1, 0.1)
	}

	def "Lighting with the surface in shadow"() {
		given:
		def m = new Material()
		def position = h.point(0, 0, 0)
		def eyev = h.vector(0, 0, -1)
		def normalv = h.vector(0, 0, -1)
		def light = new PointLight(h.point(0, 0, -10), new Color(1, 1, 1))
		def in_shadow = true

		when:
		def result = Lighting.lighting(m, light, position, eyev, normalv, in_shadow)

		then:
		result == new Color(0.1, 0.1, 0.1)
	}
}
