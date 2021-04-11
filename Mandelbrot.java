
public class Mandelbrot {
	public static final int MAX_DEPTH = 2000;
	
	
	public static int altApply(ComplexNumber point) {
		ComplexNumber start = new ComplexNumber();
		int depth = 1;
		ComplexNumber z_n = (start.multiply(start)).add(point);
		if (z_n.getMagnitude() >= 2) return 0;
//		System.out.println(z_n);
		for (int i = 1; depth < MAX_DEPTH && z_n.getMagnitude() < 2.0; i++) {
//			System.out.println("" + z_n + "                 " + depth);
			z_n = z_n.pow(2).add(point);
			++depth;
		}
		
		return depth;
	}
//	
	
	//////////////////////////////////////////////////////////////////////////////////////////////	
	private static int mandelbrot(ComplexNumber input, ComplexNumber seed, int depth) {
		if(input.getMagnitude() >= 2.0 || depth >= MAX_DEPTH) {
			return 0;
		}
		System.out.println("" +input + "                 " + depth);
		return 1 + mandelbrot((input.pow(2)).add(seed), seed, depth + 1);
	}
	
	public static int apply(ComplexNumber seed) {
		
		return mandelbrot(new ComplexNumber(), seed, 0);
	}

}
