package som;

import java.util.Vector;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class SombieVector extends Vector<Double> {

    public SombieVector() {

    }

    /**
     * Returns the squared distance between this and the other Vector, -999 if they differ in size().
     * @param other
     * @return
     */
    public double euclidianDistanceSquared(SombieVector other) {
	if (other.size() != size())
	    return -999;
	double out = 0;
	double temp;
	for (int x = 0; x < size(); x++) {
	    temp = elementAt(x).doubleValue() - other.elementAt(x).doubleValue();
	    temp *= temp;
	    out += temp;
	}
	return out;
    }
    
    public double getDistance(SombieVector other) {
	return getDistance(other, 0);
    }

    public double getDistance(SombieVector other, int t) {
	double[] otherarray = new double[other.size()];
	double[] thisarray = new double[this.size()];
	for (int i=0; i<other.size(); i++) {
	    otherarray[i] = other.elementAt(i).doubleValue();
	    thisarray[i] = this.elementAt(i).doubleValue();
	}
	double out = -999;
	
	switch(t) {
	case 0:
	    EuclideanDistance ed = new EuclideanDistance();
	    out = ed.compute(thisarray, otherarray);
	    break;
	case 1:
	    double d = getCosineDistance(thisarray, otherarray);
	    //1 means same, 0 means perpendicular, -1 means opposite
	    out = 1 - d;
	    break;
	case 2:
	    break;
	}
	return out;
    }
    
    /**
     * 
     * @param one first Vector
     * @param two second Vector
     * @return cosine similarity as defined by <pre>dot(one,two) / ||one|| * ||two||</pre>
     * returns 1 for same direction (similar)<br>
     * returns 0 for perpendicular<br>
     * returns -1 for opposite direction
     */
    private double getCosineDistance(double[] one, double[] two) {
	double mag1 = 0;
	for (double d : one) {
	    mag1 += d*d;
	}
	mag1 = Math.pow(mag1, 0.5);
	double mag2 = 0;
	for (double d : two) {
	    mag2 += d*d;
	}
	mag2 = Math.pow(mag2, 0.5);
	
	double dot = 0;
	for (int i=0; i<one.length; i++) {
	    dot += one[i]*two[i];
	}
	return dot / (mag1*mag2);
    }
}
