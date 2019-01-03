import java.lang.Math
import com.karchner.Tuples.*

class TuplesTest extends GroovyTestCase {
	void 'test A tuple with w=1.0 is a point' () {
		def t = new RTTuple(4.3,-4.2,3.1, 1.0)
		assertEquals(t.x,4.3)
		assert t.y == -4.2
		assert t.z == 3.1
		assert t.w == 1.0
		assert t.isPoint()
		assert !t.isVector()
	}

	void 'test A tuple with w=0 is a vector' () {
		def t = new RTTuple(4.3,-4.2,3.1, 0.0)
		assert t.x == 4.3
		assert t.y == -4.2
		assert t.z == 3.1
		assert t.w == 0.0
		assert !t.isPoint()
		assert t.isVector()
	}

	void 'test point() creates RTTuple with w == 1.0' () {
		def helpers = new TupleHelpers()
		def p = helpers.point(4.3,-4.2,3.1)
		assert p.isPoint()
		assert !p.isVector()
		assert p.w == 1.0

	}

	void 'test vector() creates RTTuple with w == 0.0' () {
		def helpers = new TupleHelpers()
		def v = helpers.vector(4.3,-4.2,3.1)
		assert !v.isPoint()
		assert v.isVector()
		assert v.w == 0.0

	}

	void 'test adding two tuples' (){
		def a1 = new RTTuple(3, -2, 5, 1)
		def a2 = new RTTuple(-2,3,1,0)
		assert	a1+a2 == new RTTuple(1,1,6,1)

	}
	void 'test subtracting two points' (){
		def helpers = new TupleHelpers()
		def p1 = helpers.point(3,2,1)
		def p2 = helpers.point(5,6,7)
		assert	p1-p2 == helpers.vector(-2,-4,-6)

	}
	void 'test subtracting a vector from a point' (){
		def helpers = new TupleHelpers()
		def p1 = helpers.point(3,2,1)
		def v1 = helpers.vector(5,6,7)
		assert	p1-v1 == helpers.point(-2,-4,-6)

	}
	void 'test subtracting two vectors' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(3,2,1)
		def v2 = helpers.vector(5,6,7)
		assert	v1-v2 == helpers.vector(-2,-4,-6)

	}
	void 'test negating a tuple' (){
		def t = new RTTuple(1,-2,3,-4)
		assert	-t == new RTTuple(-1,2,-3,4)

	}

	void 'test multiplying a tuple by a scalar' (){
		def t = new RTTuple(1,-2,3,-4)
		assert	t * 3.5 == new RTTuple(3.5,-7,10.5,-14)

	}
	void 'test multiplying a tuple by a fraction' (){
		def t = new RTTuple(1,-2,3,-4)
		assert	t * 0.5 == new RTTuple(0.5,-1,1.5,-2)

	}
	void 'test dividing a tuple by a scalar' (){
		def t = new RTTuple(1,-2,3,-4)
		assert	t /2 == new RTTuple(0.5,-1,1.5,-2)

	}
	void 'test Computing the magnitude of vector(1, 0, 0)' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,0,0)
		assert helpers.magnitude(v1) == 1
	}
	void 'test Computing the magnitude of vector(0, 1, 0)' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(0,1,0)
		assert helpers.magnitude(v1) == 1
	}
	void 'test Computing the magnitude of vector(0, 0, 1)' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(0,0,1)
		assert helpers.magnitude(v1) == 1
	}
	void 'test Computing the magnitude of vector(1,2,3)' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,2,3)
		assert helpers.magnitude(v1) == Math.sqrt(14)
	}
	void 'test Computing the magnitude of vector(3,2,1)' (){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(3,2,1)
		assert helpers.magnitude(v1) == Math.sqrt(14)
	}

	void 'test Normalizing vector(4, 0, 0) gives (1, 0, 0)'(){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(4,0,0)
		assert helpers.normalize(v1) == helpers.vector(1,0,0)
	}
	void 'test Normalizing vector(1,2,3)'(){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,2,3)
		assert helpers.normalize(v1) == helpers.vector(0.26726, 0.53452, 0.80178)
	}
	void 'test magnitude of a normalized vector'(){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,2,3)
		def norm = helpers.normalize(v1)
		assert helpers.magnitude(norm) == 1
	}

	void 'test the dot product of two tuples'(){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,2,3)
		def v2 = helpers.vector(2,3,4)

		assert helpers.dot(v1,v2) == 20
	}	
	void 'test the cross product of two vectors'(){
		def helpers = new TupleHelpers()
		def v1 = helpers.vector(1,2,3)
		def v2 = helpers.vector(2,3,4)

		assert helpers.cross(v1,v2) == helpers.vector(-1, 2, -1)
		assert helpers.cross(v2,v1) == helpers.vector(1, -2, 1)
	}	

}
