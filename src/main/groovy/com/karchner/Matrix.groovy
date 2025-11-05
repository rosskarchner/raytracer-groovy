package com.karchner
import com.karchner.Tuples.RTTuple
import java.lang.Math

class Matrix {
	private List<List<Double>> data
	private int rows
	private int cols
	private static final double EPSILON = 0.00001

	Matrix(List<List<Number>> input) {
		this.data = input.collect { row ->
			row.collect { it.toDouble() }
		}
		this.rows = data.size()
		this.cols = data[0].size()
	}

	double at(int row, int col) {
		return data[row][col]
	}

	int getRows() { return rows }
	int getCols() { return cols }

	boolean equals(Object other) {
		if (!(other instanceof Matrix)) {
			return false
		}
		Matrix m = (Matrix) other
		if (this.rows != m.rows || this.cols != m.cols) {
			return false
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (Math.abs(this.at(i, j) - m.at(i, j)) > EPSILON) {
					return false
				}
			}
		}
		return true
	}

	Matrix multiply(Matrix other) {
		if (this.cols != other.rows) {
			throw new IllegalArgumentException("Matrix dimensions don't match for multiplication")
		}

		def result = []
		for (int i = 0; i < this.rows; i++) {
			def row = []
			for (int j = 0; j < other.cols; j++) {
				def sum = 0.0
				for (int k = 0; k < this.cols; k++) {
					sum += this.at(i, k) * other.at(k, j)
				}
				row << sum
			}
			result << row
		}
		return new Matrix(result)
	}

	RTTuple multiply(RTTuple tuple) {
		if (this.rows != 4 || this.cols != 4) {
			throw new IllegalArgumentException("Matrix must be 4x4 to multiply with tuple")
		}

		def result = []
		for (int i = 0; i < 4; i++) {
			def sum = 0.0
			for (int j = 0; j < 4; j++) {
				sum += this.at(i, j) * tuple[j]
			}
			result << sum
		}
		return new RTTuple(result[0], result[1], result[2], result[3])
	}

	static Matrix identity() {
		return new Matrix([
			[1, 0, 0, 0],
			[0, 1, 0, 0],
			[0, 0, 1, 0],
			[0, 0, 0, 1]
		])
	}

	Matrix transpose() {
		def result = []
		for (int j = 0; j < cols; j++) {
			def row = []
			for (int i = 0; i < rows; i++) {
				row << this.at(i, j)
			}
			result << row
		}
		return new Matrix(result)
	}

	double determinant() {
		if (rows == 2 && cols == 2) {
			return at(0, 0) * at(1, 1) - at(0, 1) * at(1, 0)
		}

		def det = 0.0
		for (int col = 0; col < cols; col++) {
			det += at(0, col) * cofactor(0, col)
		}
		return det
	}

	Matrix submatrix(int removeRow, int removeCol) {
		def result = []
		for (int i = 0; i < rows; i++) {
			if (i == removeRow) continue
			def row = []
			for (int j = 0; j < cols; j++) {
				if (j == removeCol) continue
				row << at(i, j)
			}
			result << row
		}
		return new Matrix(result)
	}

	double minor(int row, int col) {
		def sub = submatrix(row, col)
		return sub.determinant()
	}

	double cofactor(int row, int col) {
		def minorValue = minor(row, col)
		if ((row + col) % 2 == 1) {
			return -minorValue
		}
		return minorValue
	}

	boolean isInvertible() {
		return determinant() != 0.0
	}

	Matrix inverse() {
		if (!isInvertible()) {
			throw new IllegalArgumentException("Matrix is not invertible")
		}

		def det = determinant()
		def result = []

		for (int row = 0; row < rows; row++) {
			def resultRow = []
			for (int col = 0; col < cols; col++) {
				def c = cofactor(col, row)  // Note: col and row are swapped for transpose
				resultRow << (c / det)
			}
			result << resultRow
		}

		return new Matrix(result)
	}
}
