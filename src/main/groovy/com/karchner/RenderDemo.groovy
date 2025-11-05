package com.karchner

import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color
import com.karchner.Canvas.Canvas

class RenderDemo {
	static void main(String[] args) {
		TupleHelpers h = new TupleHelpers()

		// Create a world with two spheres
		def world = new World()
		world.light = new PointLight(h.point(-10, 10, -10), new Color(1, 1, 1))

		// Floor sphere - flattened and scaled
		def floor = new Sphere()
		floor.transform = Transformation.scaling(10, 0.01, 10)
		floor.material.color = new Color(1, 0.9, 0.9)
		floor.material.specular = 0

		// Left wall
		def left_wall = new Sphere()
		left_wall.transform = Transformation.translation(0, 0, 5) *
		                      Transformation.rotation_y(-Math.PI/4) *
		                      Transformation.rotation_x(Math.PI/2) *
		                      Transformation.scaling(10, 0.01, 10)
		left_wall.material = floor.material

		// Right wall
		def right_wall = new Sphere()
		right_wall.transform = Transformation.translation(0, 0, 5) *
		                       Transformation.rotation_y(Math.PI/4) *
		                       Transformation.rotation_x(Math.PI/2) *
		                       Transformation.scaling(10, 0.01, 10)
		right_wall.material = floor.material

		// Large sphere in the center
		def middle = new Sphere()
		middle.transform = Transformation.translation(-0.5, 1, 0.5)
		middle.material.color = new Color(0.1, 1, 0.5)
		middle.material.diffuse = 0.7
		middle.material.specular = 0.3

		// Smaller green sphere on the right
		def right = new Sphere()
		right.transform = Transformation.translation(1.5, 0.5, -0.5) *
		                  Transformation.scaling(0.5, 0.5, 0.5)
		right.material.color = new Color(0.5, 1, 0.1)
		right.material.diffuse = 0.7
		right.material.specular = 0.3

		// Smallest sphere on the left
		def left = new Sphere()
		left.transform = Transformation.translation(-1.5, 0.33, -0.75) *
		                 Transformation.scaling(0.33, 0.33, 0.33)
		left.material.color = new Color(1, 0.8, 0.1)
		left.material.diffuse = 0.7
		left.material.specular = 0.3

		world.objects = [floor, left_wall, right_wall, middle, right, left]

		// Set up the camera
		def camera = new Camera(400, 200, Math.PI/3)
		camera.transform = Camera.view_transform(
			h.point(0, 1.5, -5),
			h.point(0, 1, 0),
			h.vector(0, 1, 0)
		)

		// Render the scene
		println "Rendering scene..."
		def canvas = camera.render(world)

		// Save to file
		def ppm = canvas.to_ppm()
		new File("scene.ppm").text = ppm

		println "Rendered scene saved to scene.ppm"
		println "Canvas size: ${canvas.width}x${canvas.height}"
	}
}
