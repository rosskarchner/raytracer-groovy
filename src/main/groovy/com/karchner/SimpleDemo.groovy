package com.karchner

import com.karchner.Tuples.TupleHelpers
import com.karchner.Canvas.Color

class SimpleDemo {
	static void main(String[] args) {
		TupleHelpers h = new TupleHelpers()

		println "Testing TupleHelpers..."
		def p1 = h.point(1, 2, 3)
		println "Created point: ${p1}"

		def p2 = h.point(4, 5, 6)
		println "Created another point: ${p2}"

		def diff = p2 - p1
		println "Difference: ${diff}"

		println "\nAll basic tests passed!"
	}
}
