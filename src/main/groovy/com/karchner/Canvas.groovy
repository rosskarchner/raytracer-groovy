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
