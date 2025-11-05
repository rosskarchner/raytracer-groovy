package com.karchner
import static com.karchner.Tuples.*
import com.karchner.Tuples.RTTuple
import com.karchner.Tuples.TupleHelpers
import com.karchner.Matrix.*
import spock.lang.*

class MatrixTest extends Specification {
	TupleHelpers h = new TupleHelpers()

	def "Constructing and inspecting a 4x4 matrix"() {
		given:
		def M = new Matrix([
			[1, 2, 3, 4],
			[5.5, 6.5, 7.5, 8.5],
			[9, 10, 11, 12],
			[13.5, 14.5, 15.5, 16.5]
		])

		expect:
		M.at(0, 0) == 1
		M.at(0, 3) == 4
		M.at(1, 0) == 5.5
		M.at(1, 2) == 7.5
		M.at(2, 2) == 11
		M.at(3, 0) == 13.5
		M.at(3, 2) == 15.5
	}

	def "A 2x2 matrix ought to be representable"() {
		given:
		def M = new Matrix([
			[-3, 5],
			[1, -2]
		])

		expect:
		M.at(0, 0) == -3
		M.at(0, 1) == 5
		M.at(1, 0) == 1
		M.at(1, 1) == -2
	}

	def "A 3x3 matrix ought to be representable"() {
		given:
		def M = new Matrix([
			[-3, 5, 0],
			[1, -2, -7],
			[0, 1, 1]
		])

		expect:
		M.at(0, 0) == -3
		M.at(1, 1) == -2
		M.at(2, 2) == 1
	}

	def "Matrix equality with identical matrices"() {
		given:
		def A = new Matrix([
			[1, 2, 3, 4],
			[5, 6, 7, 8],
			[9, 8, 7, 6],
			[5, 4, 3, 2]
		])
		def B = new Matrix([
			[1, 2, 3, 4],
			[5, 6, 7, 8],
			[9, 8, 7, 6],
			[5, 4, 3, 2]
		])

		expect:
		A == B
	}

	def "Matrix equality with different matrices"() {
		given:
		def A = new Matrix([
			[1, 2, 3, 4],
			[5, 6, 7, 8],
			[9, 8, 7, 6],
			[5, 4, 3, 2]
		])
		def B = new Matrix([
			[2, 3, 4, 5],
			[6, 7, 8, 9],
			[8, 7, 6, 5],
			[4, 3, 2, 1]
		])

		expect:
		A != B
	}

	def "Multiplying two matrices"() {
		given:
		def A = new Matrix([
			[1, 2, 3, 4],
			[5, 6, 7, 8],
			[9, 8, 7, 6],
			[5, 4, 3, 2]
		])
		def B = new Matrix([
			[-2, 1, 2, 3],
			[3, 2, 1, -1],
			[4, 3, 6, 5],
			[1, 2, 7, 8]
		])

		when:
		def result = A * B

		then:
		result == new Matrix([
			[20, 22, 50, 48],
			[44, 54, 114, 108],
			[40, 58, 110, 102],
			[16, 26, 46, 42]
		])
	}

	def "A matrix multiplied by a tuple"() {
		given:
		def A = new Matrix([
			[1, 2, 3, 4],
			[2, 4, 4, 2],
			[8, 6, 4, 1],
			[0, 0, 0, 1]
		])
		def b = h.point(1, 2, 3)

		when:
		def result = A * b

		then:
		result == h.point(18, 24, 33)
	}

	def "Multiplying a matrix by the identity matrix"() {
		given:
		def A = new Matrix([
			[0, 1, 2, 4],
			[1, 2, 4, 8],
			[2, 4, 8, 16],
			[4, 8, 16, 32]
		])

		when:
		def result = A * Matrix.identity()

		then:
		result == A
	}

	def "Multiplying the identity matrix by a tuple"() {
		given:
		def a = h.point(1, 2, 3)

		when:
		def result = Matrix.identity() * a

		then:
		result == a
	}

	def "Transposing a matrix"() {
		given:
		def A = new Matrix([
			[0, 9, 3, 0],
			[9, 8, 0, 8],
			[1, 8, 5, 3],
			[0, 0, 5, 8]
		])

		when:
		def result = A.transpose()

		then:
		result == new Matrix([
			[0, 9, 1, 0],
			[9, 8, 8, 0],
			[3, 0, 5, 5],
			[0, 8, 3, 8]
		])
	}

	def "Transposing the identity matrix"() {
		given:
		def A = Matrix.identity()

		when:
		def result = A.transpose()

		then:
		result == Matrix.identity()
	}

	def "Calculating the determinant of a 2x2 matrix"() {
		given:
		def A = new Matrix([
			[1, 5],
			[-3, 2]
		])

		expect:
		A.determinant() == 17
	}

	def "A submatrix of a 3x3 matrix is a 2x2 matrix"() {
		given:
		def A = new Matrix([
			[1, 5, 0],
			[-3, 2, 7],
			[0, 6, -3]
		])

		when:
		def result = A.submatrix(0, 2)

		then:
		result == new Matrix([
			[-3, 2],
			[0, 6]
		])
	}

	def "A submatrix of a 4x4 matrix is a 3x3 matrix"() {
		given:
		def A = new Matrix([
			[-6, 1, 1, 6],
			[-8, 5, 8, 6],
			[-1, 0, 8, 2],
			[-7, 1, -1, 1]
		])

		when:
		def result = A.submatrix(2, 1)

		then:
		result == new Matrix([
			[-6, 1, 6],
			[-8, 8, 6],
			[-7, -1, 1]
		])
	}

	def "Calculating a minor of a 3x3 matrix"() {
		given:
		def A = new Matrix([
			[3, 5, 0],
			[2, -1, -7],
			[6, -1, 5]
		])
		def B = A.submatrix(1, 0)

		expect:
		B.determinant() == 25
		A.minor(1, 0) == 25
	}

	def "Calculating a cofactor of a 3x3 matrix"() {
		given:
		def A = new Matrix([
			[3, 5, 0],
			[2, -1, -7],
			[6, -1, 5]
		])

		expect:
		A.minor(0, 0) == -12
		A.cofactor(0, 0) == -12
		A.minor(1, 0) == 25
		A.cofactor(1, 0) == -25
	}

	def "Calculating the determinant of a 3x3 matrix"() {
		given:
		def A = new Matrix([
			[1, 2, 6],
			[-5, 8, -4],
			[2, 6, 4]
		])

		expect:
		A.cofactor(0, 0) == 56
		A.cofactor(0, 1) == 12
		A.cofactor(0, 2) == -46
		A.determinant() == -196
	}

	def "Calculating the determinant of a 4x4 matrix"() {
		given:
		def A = new Matrix([
			[-2, -8, 3, 5],
			[-3, 1, 7, 3],
			[1, 2, -9, 6],
			[-6, 7, 7, -9]
		])

		expect:
		A.cofactor(0, 0) == 690
		A.cofactor(0, 1) == 447
		A.cofactor(0, 2) == 210
		A.cofactor(0, 3) == 51
		A.determinant() == -4071
	}

	def "Testing an invertible matrix for invertibility"() {
		given:
		def A = new Matrix([
			[6, 4, 4, 4],
			[5, 5, 7, 6],
			[4, -9, 3, -7],
			[9, 1, 7, -6]
		])

		expect:
		A.determinant() == -2120
		A.isInvertible()
	}

	def "Testing a noninvertible matrix for invertibility"() {
		given:
		def A = new Matrix([
			[-4, 2, -2, -3],
			[9, 6, 2, 6],
			[0, -5, 1, -5],
			[0, 0, 0, 0]
		])

		expect:
		A.determinant() == 0
		!A.isInvertible()
	}

	def "Calculating the inverse of a matrix"() {
		given:
		def A = new Matrix([
			[-5, 2, 6, -8],
			[1, -5, 1, 8],
			[7, 7, -6, -7],
			[1, -3, 7, 4]
		])

		when:
		def B = A.inverse()

		then:
		A.determinant() == 532
		A.cofactor(2, 3) == -160
		B.at(3, 2) == -160.0 / 532
		A.cofactor(3, 2) == 105
		B.at(2, 3) == 105.0 / 532
		B == new Matrix([
			[0.21805, 0.45113, 0.24060, -0.04511],
			[-0.80827, -1.45677, -0.44361, 0.52068],
			[-0.07895, -0.22368, -0.05263, 0.19737],
			[-0.52256, -0.81391, -0.30075, 0.30639]
		])
	}

	def "Calculating the inverse of another matrix"() {
		given:
		def A = new Matrix([
			[8, -5, 9, 2],
			[7, 5, 6, 1],
			[-6, 0, 9, 6],
			[-3, 0, -9, -4]
		])

		when:
		def result = A.inverse()

		then:
		result == new Matrix([
			[-0.15385, -0.15385, -0.28205, -0.53846],
			[-0.07692, 0.12308, 0.02564, 0.03077],
			[0.35897, 0.35897, 0.43590, 0.92308],
			[-0.69231, -0.69231, -0.76923, -1.92308]
		])
	}

	def "Calculating the inverse of a third matrix"() {
		given:
		def A = new Matrix([
			[9, 3, 0, 9],
			[-5, -2, -6, -3],
			[-4, 9, 6, 4],
			[-7, 6, 6, 2]
		])

		when:
		def result = A.inverse()

		then:
		result == new Matrix([
			[-0.04074, -0.07778, 0.14444, -0.22222],
			[-0.07778, 0.03333, 0.36667, -0.33333],
			[-0.02901, -0.14630, -0.10926, 0.12963],
			[0.17778, 0.06667, -0.26667, 0.33333]
		])
	}

	def "Multiplying a product by its inverse"() {
		given:
		def A = new Matrix([
			[3, -9, 7, 3],
			[3, -8, 2, -9],
			[-4, 4, 4, 1],
			[-6, 5, -1, 1]
		])
		def B = new Matrix([
			[8, 2, 2, 2],
			[3, -1, 7, 0],
			[7, 0, 5, 4],
			[6, -2, 0, 5]
		])
		def C = A * B

		expect:
		C * B.inverse() == A
	}
}
