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

class CanvasTest extends GroovyTestCase{
	void 'test creating a canvas' (){
		def c = new Canvas(10,20)
		def black = new Color(0,0,0)

		assert c.width == 10
		assert c.height == 20
		for (row in c.pixels) {
			for (pixel in row){
				assert pixel == black
			}
		}

	}


	void 'test writing a pixel' (){
		def c = new Canvas(10,20)
		def red = new Color(1,0,0)
		c.write_pixel(2,3, red)
		assert c.pixel_at(2,3) == red
	}

	void 'test constucting PPM header' (){
		def c = new Canvas(5,3)
		def ppm = c.to_ppm()
		def lines = ppm.readLines()	
		assert lines[0] == 'P3'
		assert lines[1] == '5 3'
		assert lines[2] == '255'

	}

	void 'test constructing PPM data' (){

		def c = new Canvas(5,3)

		def c1 = new Color(1.5,0,0)
		def c2 = new Color(0,0.5,0)
		def c3 = new Color(-0.5, 0, 1)
		c.write_pixel(0, 0, c1)
		c.write_pixel(2, 1, c2)
		c.write_pixel(4, 2, c3)
		def ppm = c.to_ppm()
		def lines = ppm.readLines()	
		assert lines.size() == 6
		assert lines[3] == '255 0 0 0 0 0 0 0 0 0 0 0 0 0 0'
		assert lines[4] == '0 0 0 0 0 0 0 128 0 0 0 0 0 0 0'
		assert lines[5] == '0 0 0 0 0 0 0 0 0 0 0 0 0 0 255'


	}

	void 'test splitting long PPM lines' (){
		def c = new Canvas(10,2)
		c.fill_canvas(new Color(1, 0.8, 0.6))

		def ppm = c.to_ppm()
		def lines = ppm.readLines()	
		def selection = lines[3..6].join('\n')

		def expected = """255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
    |153 255 204 153 255 204 153 255 204 153 255 204 153
    |255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
    |153 255 204 153 255 204 153 255 204 153 255 204 153""".stripMargin()
		assert selection == expected
	}

	void 'test that PPM files are terminated with a newline' (){
		def c = new Canvas(5,3)

		def ppm = c.to_ppm()

		assert ppm[-1] == '\n'


	}
}


