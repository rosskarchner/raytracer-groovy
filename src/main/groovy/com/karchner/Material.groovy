package com.karchner
import com.karchner.Canvas.Color

class Material {
	Color color
	double ambient
	double diffuse
	double specular
	double shininess

	Material() {
		this.color = new Color(1, 1, 1)
		this.ambient = 0.1
		this.diffuse = 0.9
		this.specular = 0.9
		this.shininess = 200.0
	}

	boolean equals(Object other) {
		if (!(other instanceof Material)) {
			return false
		}
		Material m = (Material) other
		return this.color == m.color &&
			   Math.abs(this.ambient - m.ambient) < 0.00001 &&
			   Math.abs(this.diffuse - m.diffuse) < 0.00001 &&
			   Math.abs(this.specular - m.specular) < 0.00001 &&
			   Math.abs(this.shininess - m.shininess) < 0.00001
	}
}
