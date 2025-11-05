package com.karchner
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color
import java.lang.Math

class Lighting {
	private static TupleHelpers h = new TupleHelpers()

	static Color lighting(Material material, PointLight light, RTTuple point,
	                      RTTuple eyev, RTTuple normalv, boolean in_shadow) {
		def helper = new TupleHelpers()
		// Combine the surface color with the light's color/intensity
		def effective_color = material.color * light.intensity

		// Find the direction to the light source
		def lightv = helper.normalize(light.position - point)

		// Compute the ambient contribution
		def ambient = effective_color * material.ambient

		// If in shadow, return only ambient lighting
		if (in_shadow) {
			return ambient
		}

		// light_dot_normal represents the cosine of the angle between the
		// light vector and the normal vector. A negative number means the
		// light is on the other side of the surface.
		def light_dot_normal = helper.dot(lightv, normalv)

		Color diffuse
		Color specular

		if (light_dot_normal < 0) {
			diffuse = new Color(0, 0, 0)
			specular = new Color(0, 0, 0)
		} else {
			// Compute the diffuse contribution
			diffuse = effective_color * material.diffuse * light_dot_normal

			// reflect_dot_eye represents the cosine of the angle between the
			// reflection vector and the eye vector. A negative number means the
			// light reflects away from the eye.
			def reflectv = reflect(lightv.negative(), normalv)
			def reflect_dot_eye = helper.dot(reflectv, eyev)

			if (reflect_dot_eye <= 0) {
				specular = new Color(0, 0, 0)
			} else {
				// Compute the specular contribution
				def factor = Math.pow(reflect_dot_eye, material.shininess)
				specular = light.intensity * material.specular * factor
			}
		}

		// Add the three contributions together to get the final shading
		return ambient + diffuse + specular
	}

	static RTTuple reflect(RTTuple vector, RTTuple normal) {
		def helper = new TupleHelpers()
		return vector - normal * 2 * helper.dot(vector, normal)
	}
}
