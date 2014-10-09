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
	// for (int i=0; i<nx*ny; i++) {
	// PImage pic = createImage(4,4, RGB);
	// pic.loadPixels();
	// SombieVector sv = sl.getNode(i).getVector();
	//
	// int d = color(0);
	// for (int j=0; j<pic.pixels.length; j++) pic.pixels[j] = color(255);
	// for (int j=0; j<dim; j++) {
	// boolean b = sv.get(j).doubleValue()>0.5;
	// int x = j%3;
	// int y = j/3;
	// int index = (y+1)*8 + x+1;
	// int ind2 = (y+1)*8 + (6-x);
	// if (b) {
	// pic.pixels[index] = color(255);
	// pic.pixels[ind2] = color(255);
	// }
	// else {
	// pic.pixels[index] = d;
	// pic.pixels[ind2] = d;
	// }
	// }
	// pic.updatePixels();
	// image(pic,i%nx*tx,i/nx*ty,tx,ty);
	// }

    }

    public void keyPressed() {
	switch(key) {
	case 'g':
	    go = !go;
	    break;
	}
    }
}
