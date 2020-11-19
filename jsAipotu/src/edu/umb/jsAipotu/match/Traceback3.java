package edu.umb.jsAipotu.match;

//Traceback3 objects for affine gap costs

class Traceback3 extends Traceback {
	int k;
	
	public Traceback3(int k, int i, int j)
	{ this.k = k; this.i = i; this.j = j; }
}
