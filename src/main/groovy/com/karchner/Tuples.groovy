package com.karchner.Tuples
import java.lang.Math

@groovy.transform.InheritConstructors
class RTTuple extends Tuple implements Comparable{

		
	RTTuple(Number... nums){

	super(nums.collect{
		it.toDouble()
		})
	}
	def getx(){
		return this[0]
	}

	def gety(){
		return this[1]
	}
	def getz(){
		return this[2]
	}
	def getw(){
		return this[3]
	}

	int compareTo(other){
	// TODO: support other comparisons
		if (this.getClass() != other.getClass()){
			return 1
		}
		def EPSILON=0.00001

		def differences = [0,1,2,3].collect{
		this[it] - other[it]	
			}
	 	if (differences.any { it > EPSILON}){
			return 1
		} else {
			return 0
		}


	}
	def plus(RTTuple othertuple){
		return new RTTuple(
			this[0]+othertuple[0],
			this[1]+othertuple[1],
			this[2]+othertuple[2],
			this[3]+othertuple[3]
			)
				

	}

	def minus(RTTuple othertuple){
		return new RTTuple(
			this[0]-othertuple[0],
			this[1]-othertuple[1],
			this[2]-othertuple[2],
			this[3]-othertuple[3]
			)
				

	}

	def multiply(Number n){
		def n_double = n.toDouble()
		return new RTTuple(
			this[0] *n_double,
			this[1] *n_double,
			this[2] *n_double,
			this[3] *n_double
			)

	}

	def div(Number n){
		def n_double = n.toDouble()
		return new RTTuple(
			this[0] /n_double,
			this[1] /n_double,
			this[2] /n_double,
			this[3] /n_double
			)

	}
	
	def equals(RTTuple otherTuple){
		return true	
	}
	RTTuple negative(){
		return new RTTuple(0,0,0,0).minus(this)
	}

	boolean isPoint(){
		return this[3] == 1.0
	}

	boolean isVector(){

		return this[3] == 0.0
	}
}

class TupleHelpers {
	// seems like these should be alternate constructors
       // for RTTuple
	public RTTuple point (double x, double y, double z){
		new RTTuple(x,y,z,1.0)
	}

	public RTTuple vector (double x, double y, double z){
		new RTTuple(x,y,z,0.0)
	}

	public RTTuple normalize(RTTuple v){
	def mag = this.magnitude(v)
	return v / mag
		

	}
	public double magnitude(RTTuple v){
		return Math.sqrt(v.x**2 +  v.y**2 + v.z**2)
	}

	def dot(RTTuple a, RTTuple b){
		[0,1,2,3].collect {a[it] * b[it]}.sum()

	}
	
	def cross(RTTuple a, RTTuple b){
		return this.vector(a.y * b.z - a.z * b.y,
			a.z * b.x - a.x * b.z,
			a.x * b.y - a.y * b.x)

	}
}
