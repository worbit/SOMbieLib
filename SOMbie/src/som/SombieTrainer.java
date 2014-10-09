package som;

import java.util.Collections;
import java.util.List;

public class SombieTrainer {

    private SombieLattice lattice;
    private List<SombieSample> vectors;

    private int latticeWidth;
    private int latticeHeight;
    private int featureDimensionality;
    private int iteration;
    private double LATTICE_RADIUS;
    private double TIME_CONSTANT;
    private double learningRate;

    // These constants can be changed to play with the learning algorithm
    private static final double START_LEARNING_RATE = 0.07;
    private static final int NUM_ITERATIONS = 500;

    public SombieTrainer() {
	this.latticeWidth = 10;
	this.latticeHeight = 10;
	this.featureDimensionality = 10;
	new SombieTrainer(latticeWidth, latticeHeight, featureDimensionality);
    }

    public SombieTrainer(int w, int h, int d) {
	this.latticeWidth = w;
	this.latticeHeight = h;
	this.featureDimensionality = d;
	this.lattice = new SombieLattice(latticeWidth, latticeHeight,
		featureDimensionality);
	this.iteration = 0;

	this.initData();
    }

    public void initData() {
	LATTICE_RADIUS = Math.max(latticeHeight, latticeWidth) / 2d;
	TIME_CONSTANT = NUM_ITERATIONS / Math.log(LATTICE_RADIUS);
    }

    public void setSamples(List<SombieSample> ss) {
	this.vectors = ss;
	System.out.println(this.vectors.size());
    }

    public void train(boolean randomize) {
	if (iteration > NUM_ITERATIONS) {
	    return;
	}
	if (iteration == NUM_ITERATIONS)
	    System.out.println("DONE");

	lattice.clearCloseTos();

	Collections.shuffle(vectors);
	SombieNode bmu = null, temp = null;
	double nbhRad = getNeighborhoodRadius();
	int xs, xe, ys, ye;
	for (SombieSample v : vectors) {
	    if (randomize) {
		// randomize input in every loop, good for colors
		for (int i = 0; i < v.weights.size(); i++) {
		    v.weights.set(i, Math.random());
		}
	    }

	    bmu = lattice.getBMU(v.weights);
	    bmu.registerSample(v);
	    xs = (int) (bmu.getX() - nbhRad - 1);
	    ys = (int) (bmu.getY() - nbhRad - 1);
	    xe = (int) (bmu.getX() + nbhRad + 1);
	    ye = (int) (bmu.getY() + nbhRad + 1);
	    if (latticeHeight == 1) {
		if (ys < 0)
		    ys = 0;
		if (ye > latticeHeight)
		    ye = latticeHeight;
	    }

	    for (int i = xs; i < xe; i++) {
		for (int j = ys; j < ye; j++) {
		    temp = lattice.getNode(i, j);
		    if (temp != null) {
			double dist = temp.distanceTo(bmu);
			if (dist <= (nbhRad * nbhRad)) {
			    double dFalloff = getDistanceFalloff(dist, nbhRad);
			    temp.adjustWeights(v.weights, learningRate,
				    dFalloff);
			}
		    }
		}
	    }
	}
	iteration++;
	learningRate = START_LEARNING_RATE
		* Math.exp(-(double) iteration / NUM_ITERATIONS);

	if (iteration % 10 == 0)
	    System.out.print(".");
	if (iteration % 100 == 0)
	    System.out.println();
    }

    private double getNeighborhoodRadius() {
	return LATTICE_RADIUS * Math.exp(-iteration / TIME_CONSTANT);
    }

    private double getDistanceFalloff(double distSq, double radius) {
	double radiusSq = radius * radius;
	return Math.exp(-(distSq) / (2 * radiusSq));
    }

    public SombieLattice getLattice() {
	return lattice;
    }

    public List<SombieSample> getSamples() {
	return vectors;
    }

    public int getIteration() {
	if (iteration > NUM_ITERATIONS)
	    return 0;
	else
	    return iteration;
    }
}
