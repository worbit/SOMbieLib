package som;

public class SombieSample {

    public SombieVector weights;
    public String label;
    
    public SombieSample() {
	weights = new SombieVector();
	label = "";
    }
    
    public SombieSample(SombieVector v) {
	this.weights = v;
	this.label = "";
    }
    
}
