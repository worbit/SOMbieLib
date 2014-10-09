package som;

import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class SombieLattice {

    private SombieNode[] nodes;
    private int dimx,dimy,dim;
    private boolean torus;
    
    public SombieLattice(int x, int y, int num) {
	this.torus = false;
	this.dimx = x;
	this.dimy = y;
	this.dim = dimx * dimy;
	this.nodes = new SombieNode[this.dim];
	for (int i=0; i<this.dim; i++) {
	    SombieNode n = new SombieNode(num);
	    n.setX(i%dimx);
	    n.setY(i/dimx);
	    n.setMother(this);
	    this.nodes[i] = n;
	}
    }
    
    public SombieNode getNode(int i) {
	int index = i;
	if (index<0) {
	    if(torus) index=(index+dim)%dim;
	    else return null;
	}
	if (index>=nodes.length) {
	    if (torus) index=index%dim;
	    else return null;
	}
	return nodes[i];
    }
    
    public SombieNode getNode(int a, int b) {
	int ix = a;
	int iy = b;
	if (torus) {
	    if (ix<0) ix=(ix+dimx)%dimx;
	    if (iy<0) iy=(iy+dimy)%dimy;
	    if (ix>=dimx) ix=ix%dimx;
	    if (iy>=dimy) iy=iy%dimy;
	}
	else {
	    if (ix>=dimx) return null;
	}
//	System.out.println(a+" > "+ix+" / "+b+" > "+iy);
	int index = (iy*dimx+ix);
	return getNode(index);
    }
    
    public double[][] getDistances() {
	double[][] out = new double[dimx][dimy*2];
	for (int i=0; i<dimx; i++) {
	    double[] a = new double[dimy*2];
	    Arrays.fill(a, -1);
	    out[i] = a;
	}
	
	for (int i=0; i<dimy*2; i++) {
	    for (int j=0; j<dimx; j++) {
		SombieNode a = getNode(j,i/2);
		SombieNode o = (i%2==0 ? getNode(j+1,i/2) : getNode(j,i/2+1));
		if (a!=null && o!=null) {
		    double d = a.getVector().getDistance(o.getVector(),1); //euclidianDistanceSquared(o.getVector());
		    out[j][i] = d;
		}
	    }
	}
	
	return out;
    }
    
    public SombieUMatrix getUMatrix() {
	SombieUMatrix sum = new SombieUMatrix();
	sum.values = getDistances();
	
	double mx = Double.MIN_VALUE;
	double mn = Double.MAX_VALUE;
	for (int i=0; i<sum.values.length; i++) {
	    for (int j=0; j<sum.values[0].length; j++) {
		double v = sum.values[i][j];
		if (v<0) continue;
		if (v>mx) {
		    mx = v;
		    sum.mxc = i;
		    sum.mxr = j;
		}
		if (v<mn) {
		    mn = v;
		    sum.mnc = i;
		    sum.mnr = j;
		}
	    }
	}
	
	sum.maxval = mx;
	sum.minval = mn;
	
	return sum;
    }
    
    public void setTorus(boolean b) {
	this.torus = b;
    }
    
    public boolean getTorus() {
	return torus;
    }
    
    public int getW() {
	return dimx;
    }

    public int getH() {
	return dimy;
    }

    public SombieNode getBMU(SombieVector vec) {
	SombieNode bmu = nodes[0];
	
	double curDist = vec.euclidianDistanceSquared(bmu.getVector());
//	double curDist = vec.getDistance(bmu.getVector(), 1);
	double bestDist = curDist;
	
	for (SombieNode n : nodes) {
	    curDist = vec.euclidianDistanceSquared(n.getVector());
//	    curDist = vec.getDistance(n.getVector(), 1);
	    if (curDist<bestDist) {
		bmu = n;
		bestDist = curDist;
	    }
	}
	
	return bmu;
    }

    public void clearCloseTos() {
	for (SombieNode n : nodes) {
	    n.clearCloseTos();
	}
    }

}
