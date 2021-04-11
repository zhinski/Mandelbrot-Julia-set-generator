import java.awt.Color;
import java.util.ArrayList;

public class InterPol {
	private ArrayList<Color> colors;
	private ArrayList<Double> iterNum;
	private ArrayList<Double> denominators;
	private ArrayList<Double> R_Value;
	private ArrayList<Double> G_Value;
	private ArrayList<Double> B_Value;
	
	
	
	public InterPol(Color...colors) {
		generatePoly(colors);
	}
	
	public void generatePoly(Color...colors) {
		// Get colors
		iterNum = new ArrayList<>();
		ArrayList<Color> colorArray = new ArrayList<>();
		for (int i = 0; i < colors.length; i++) {
			if (colors[i] != null) colorArray.add(colors[i]);
		}
		// add the last color, which is always black
		colorArray.add(Color.BLACK);
		this.colors = colorArray;
//		System.out.println(this.colors);///////////////////////////////////////////////////////////////////////////////////////////////////
		// Store R-Values
		R_Value = new ArrayList<>();
		for (int i = 0; i < colorArray.size(); i++)			R_Value.add(((double) colorArray.get(i).getRed()));

		// Store G-Values
		G_Value = new ArrayList<>();
		for (int i = 0; i < colorArray.size(); i++)			G_Value.add(((double) colorArray.get(i).getGreen()));

		// Store B-Values
		B_Value = new ArrayList<>();
		for (int i = 0; i < colorArray.size(); i++)			B_Value.add(((double) colorArray.get(i).getBlue()));
		
		// Store x-Values
		double intervalLength = Mandelbrot.MAX_DEPTH / ((double) colorArray.size() - 1);
		for (double i = 0; i < colorArray.size() - 1; i++) {
			iterNum.add(i*intervalLength);
		}
		iterNum.add((double) Mandelbrot.MAX_DEPTH);
//		System.out.println(iterNum);///////////////////////////////////////////////////////////////////////////////////////////////////
		// Calculate denominators
		denominators = new ArrayList<>();
		double denom;
		for (int j = 0; j < colorArray.size(); j++) {
			denom = 1;
			for (int i = 0; i < colorArray.size(); i++) {
				if (i != j) 	denom *= (iterNum.get(j) - iterNum.get(i));
			}
			denominators.add(denom);
		}
//		System.out.println(denominators);///////////////////////////////////////////////////////////////////////////////////////////////////
		
//		System.out.println("");///////////////////////////////////////////////////////////////////////////////////////////////////
	}
	
	public Color iterToColor(int iter) {
		double it = (double) iter;
		return new Color(calculateRed(it), calculateGreen(it), calculateBlue(it));
	}
	
	private double calculatePolyTerm(double iter, int termNum) {
		double result = 1;
		for (int i = 0; i < colors.size(); i++) {
			if (i != termNum) result *= (iter - iterNum.get(i));
		}
		result /= denominators.get(termNum);
		return result;
	}
	
	private int calculateRed(double iter) {
		double redVal = 0;
		for (int i = 0; i < colors.size(); i++) {
			redVal += R_Value.get(i)*calculatePolyTerm(iter, i);
		}
		if (redVal > 255) return 255;
		else if (redVal < 0) return 0;
		return (int) redVal;
	}
	
	private int calculateGreen(double iter) {
		double greenVal = 0;
		for (int i = 0; i < colors.size(); i++) {
			greenVal += G_Value.get(i)*calculatePolyTerm(iter, i);
		}
		if (greenVal > 255) return 255;
		else if (greenVal < 0) return 0;
		return (int) greenVal;
	}
	
	private int calculateBlue(double iter) {
		double blueVal = 0;
		for (int i = 0; i < colors.size(); i++) {
			blueVal += B_Value.get(i)*calculatePolyTerm(iter, i);
		}
		if (blueVal > 255) return 255;
		else if (blueVal < 0) return 0;
		return (int) blueVal;
	}
}
