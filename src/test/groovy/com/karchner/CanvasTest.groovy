import com.karchner.Canvas.*

class ColorTest extends GroovyTestCase {
	void 'test adding colors' () {
		def c1 = new Color(0.9, 0.6, 0.75)
		def c2 = new Color(0.7, 0.1, 0.25)
		assert c1 + c2 == new Color(1.6, 0.7, 1.0)
	}

	void 'test subtracting colors' () {
		def c1 = new Color(0.9, 0.6, 0.75)
		def c2 = new Color(0.7, 0.1, 0.25)
		assert c1 - c2 == new Color(0.2, 0.5, 0.5)
	}

	void 'test multiplying colors by a scalar' () {
		def c = new Color(0.2, 0.3, 0.4)
		assert c*2  == new Color(0.4, 0.6, 0.8)
	}

	void 'test multiplying colors' () {
		def c1 = new Color(1, 0.2, 0.4)
		def c2 = new Color(0.9, 1, 0.1)
		assert c1 * c2 == new Color(0.9, 0.2, 0.04)
	}
}
