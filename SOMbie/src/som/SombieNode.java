package som;

import java.util.ArrayList;
import java.util.List;

public class SombieNode {

    private SombieVector weights;
    private int xp, yp;
    private SombieLattice mother;
    private List<SombieSample> closeTo;
    
    public SombieNode(int dim) {
	this.weights = new SombieVector();
	
	for (int i=0; i<dim; i++) {
	    this.weights.addElement(Math.random());
	}
	
	this.closeTo = new ArrayList<SombieSample>();
    }
    
    public double distanceTo(SombieNode other) {
	double dx = Math.abs(getX()-other.getX());
	double dy = Math.abs(getY()-other.getY());
	if (mother.getTorus()) {
	    dx = Math.min(dx, mother.getW()-dx);
	    dy = Math.min(dy, mother.getH()-dy);
	}
	return dx*dx + dy*dy;
    }
        
    public SombieVector getVector() {
	return weights;
    }

    public int getX() {
	return xp;
    }

    public void setX(int xp) {
	this.xp = xp;
    }

    public int getY() {
	return yp;
    }

    public void setY(int yp) {
	this.yp = yp;
    }

    public SombieLattice getMother() {
	return mother;
    }

    public void setMother(SombieLattice mother) {
	this.mother = mother;
    }

    public void adjustWeights(SombieVector input, double learningRate, double distanceFalloff) {
	double wt, vw;
	for (int w=0; w<weights.size(); w++) {
		wt = ((Double)weights.elementAt(w)).doubleValue();
		vw = ((Double)input.elementAt(w)).doubleValue();
		wt += distanceFalloff * learningRate * (vw - wt);
		weights.setElementAt(new Double(wt), w);
	}
    }
    
    public void registerSample(SombieSample s) {
	this.closeTo.add(s);
    }
    
    public void clearCloseTos() {
	this.closeTo.clear();
    }
    
    public List<SombieSample> getCloseTos() {
	return this.closeTo;
    }
}
