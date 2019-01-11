package com.karchner.Canvas
import java.lang.Math

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

	def scaled(max){
		this.collect {
			def scaled_value = it * max
			if (scaled_value < 0) 0
			else if (scaled_value > max) max
			else Math.round(scaled_value)
		}

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

		this.fill_canvas(black)

	}

	def write_pixel(int column, int row, Color color){
		this.pixels[row][column] = color
	}		
	
	def fill_canvas(Color color){
		for (row in 0..this.height-1){
			for (column in 0..this.width-1){
				this.pixels[row][column] = color
			}
		}


	}

	def pixel_at(int column,int row){
		this.pixels[row][column]
	}


	def to_ppm(){
		def ppm = """\
		|P3
		|${this.width} ${this.height}
		|255""".stripMargin()

		for (row in 0..height-1){
			def ppm_data = []
			for (pixel in this.pixels[row]){
				def scaled = pixel.scaled(255)
				ppm_data += scaled
			}
			def ppm_text_lines = [""]
			for (value in ppm_data){
				if (ppm_text_lines[-1].length() + value.toString().length() <= 70){

				ppm_text_lines[-1] += value.toString() + " "

				} else {

					ppm_text_lines << value + " "
				}
			}
		ppm += '\n' + ppm_text_lines.collect {it.trim()}.join('\n')
		}

		ppm
	}
}
