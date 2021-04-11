
public class ComplexNumber {
	
	private double real;
	private double imaginary;
	
	public ComplexNumber(double real, double imaginary) {
		
		this.real = real;
		this.imaginary = imaginary;
		
	}
	
	public ComplexNumber() {
		real = 0;
		imaginary = 0;
	}

	public ComplexNumber add(ComplexNumber z) {
		
		this.real += z.real;
		this.imaginary += z.imaginary;
		return this;
	}
	
	public ComplexNumber subtract(ComplexNumber z) {
		this.real -= z.real;
		this.imaginary -= z.imaginary;
		return this;
	}
	
	public ComplexNumber multiply(ComplexNumber z) {
		double newReal = this.real*z.real - this.imaginary*z.imaginary;
		double newIm = this.real*z.imaginary + this.imaginary*z.real;
		this.real = newReal;
		this.imaginary = newIm;
		
		return this;
	}
	
	public ComplexNumber divide(ComplexNumber z) {
		double divisor = z.real*z.real + z.imaginary*z.imaginary;
		
		double newReal = (this.real*z.real + this.imaginary*z.imaginary) / divisor;
		double newIm = (this.imaginary*z.real - this.real*z.imaginary) / divisor;
		this.real = newReal;
		this.imaginary = newIm;
		
		return this;
	}
	
	public double getMagnitude() {
		return Math.sqrt(real*real + imaginary*imaginary);
	}
	
	public double getReal() {
		return real;
	}

	public double getImaginary() {
		return imaginary;
	}
	
	public ComplexNumber pow(int exp) {
		if(exp < 0) {
			ComplexNumber copy = new ComplexNumber(real,imaginary);
			copy.pow(-exp);
			this.real = 1.0;
			this.imaginary = 0.0;
			this.divide(copy);
			return this;
		} else if(exp == 0) {
			real = 1.0;
			imaginary = 0.0;
			return this;
		} else if(exp == 1) {
			return this;
		}
		ComplexNumber out = new ComplexNumber(this.real, this.imaginary);
		out.pow(exp - 1);
		this.multiply(out);
		return this;
	}
	
	@Override
	public String toString() {
		String str = "{" + real;
		if (imaginary < 0) {
			str = str + " - " + (-imaginary) + "i}";
		} else {
			str = str + " + " + imaginary + "i}";
		}
		
		return str;
	}
}
