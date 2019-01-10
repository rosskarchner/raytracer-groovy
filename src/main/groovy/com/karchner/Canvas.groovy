package com.karchner.Canvas

@groovy.transform.InheritConstructors
class Color extends Tuple{

		
	def getred(){
		return this[0]
	}

	def getgreen(){
		return this[1]
	}
	def getblue(){
		return this[2]
	}

	def plus(Color othertuple){
		return new Color(
			this[0]+othertuple[0],
			this[1]+othertuple[1],
			this[2]+othertuple[2],
			)
				

	}

	def minus(Color othertuple){
		return new Color(
			this[0]-othertuple[0],
			this[1]-othertuple[1],
			this[2]-othertuple[2],
			)
				

	}

	def multiply(Color c){
		def r = this.red * c.red
		def g = this.green * c.green
		def b = this.blue * c.blue
		return new Color(r,g,b)
	}
	def multiply(Number n){
		def n_double = n.toDouble()
		return new Color(
			this[0] *n_double,
			this[1] *n_double,
			this[2] *n_double,
			)

	}

}

class Canvas {

	int width
	int height
	Color[][] pixels

	Canvas(int width, int height){
		this.width = width
		this.height = height
		this.pixels = new Color[height][width]
		def black = new Color(0,0,0)

		for (row in 0..height-1){
			for (column in 0..width-1){
				this.pixels[row][column] = black	
			}
		}
	}

	def write_pixel(int column, int row, Color color){
		this.pixels[row][column] = color
	}		
	
	def pixel_at(int column,int row){
		this.pixels[row][column]
	}


	def to_ppm(){
		def ppm = """\
		|P3
		|5 3
		|255""".stripMargin()

		for (row in 0..height-1){
			def line = "\n"
			for (pixel in this.pixels[row]){
				line += "${pixel.red * 255} ${pixel.green} ${pixel.blue}"
			}
		ppm += line
		}

		ppm
	}
}
