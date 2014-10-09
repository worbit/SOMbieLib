package app;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import som.SombieLattice;
import som.SombieNode;
import som.SombieSample;
import som.SombieTrainer;
import som.SombieVector;

public class SombieApplet extends PApplet {

    SombieTrainer st;
    SombieLattice sl;
    int nx = 15;
    int ny = 15;
    int dim = 3;
    float tx, ty;
    boolean go=false;

    public void setup() {
	size(300, 300);
	noStroke();
	st = new SombieTrainer(nx, ny, dim);
	sl = st.getLattice();
	tx = width / (float) nx;
	ty = height / (float) ny;

	List<SombieSample> vectors = new ArrayList<SombieSample>();
	SombieVector sv;
	for (int i = 0; i < 10; i++) {
	    sv = new SombieVector();
	    for (int j = 0; j < dim; j++) {
		sv.addElement(Math.random());
	    }
	    vectors.add(new SombieSample(sv));
	}
	st.setSamples(vectors);
    }

    public void draw() {
	if (go) st.train(false);

	background(255, 255, 0);
	for (int i = 0; i < nx * ny; i++) {
	    SombieVector sv = sl.getNode(i).getVector();
	    float r = (float) (255 * sv.get(0).doubleValue());
	    float g = (float) (255 * sv.get(1).doubleValue());
	    float b = (float) (255 * sv.get(2).doubleValue());
	    fill(r, g, b);
	    rect(i % nx * tx, i / nx * ty, tx, ty);
	}
    }

    public void keyPressed() {
	switch(key) {
	case 'g':
	    go = !go;
	    break;
	}
    }
}
