package com.karchner
import java.lang.Math

class Transformation {

	static Matrix translation(double x, double y, double z) {
		return new Matrix([
			[1, 0, 0, x],
			[0, 1, 0, y],
			[0, 0, 1, z],
			[0, 0, 0, 1]
		])
	}

	static Matrix scaling(double x, double y, double z) {
		return new Matrix([
			[x, 0, 0, 0],
			[0, y, 0, 0],
			[0, 0, z, 0],
			[0, 0, 0, 1]
		])
	}

	static Matrix rotation_x(double radians) {
		def c = Math.cos(radians)
		def s = Math.sin(radians)
		return new Matrix([
			[1, 0, 0, 0],
			[0, c, -s, 0],
			[0, s, c, 0],
			[0, 0, 0, 1]
		])
	}

	static Matrix rotation_y(double radians) {
		def c = Math.cos(radians)
		def s = Math.sin(radians)
		return new Matrix([
			[c, 0, s, 0],
			[0, 1, 0, 0],
			[-s, 0, c, 0],
			[0, 0, 0, 1]
		])
	}

	static Matrix rotation_z(double radians) {
		def c = Math.cos(radians)
		def s = Math.sin(radians)
		return new Matrix([
			[c, -s, 0, 0],
			[s, c, 0, 0],
			[0, 0, 1, 0],
			[0, 0, 0, 1]
		])
	}

	static Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
		return new Matrix([
			[1, xy, xz, 0],
			[yx, 1, yz, 0],
			[zx, zy, 1, 0],
			[0, 0, 0, 1]
		])
	}
}
