import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class EquationParser {
	private LinkedList<String> eqn;		// Gets cleared after being used in constuctor 
	private LinkedList<LinkedList<String>> rule;
	private LinkedList<ComplexNumber> terms;
	private TreeMap<Integer, String> operators;
	private int[] operatorIndices;
	private String ogEqn;
	private ComplexNumber z = new ComplexNumber(2, -1); // DON'T FORGET TO CHANGE THIS
	private LinkedList<String> plusOrMinusOrder;
	
	/**
	 * <p><strong> To use as a calculator/test for bugs, use the following code 
	 * (with your own expression and input ComplexNumber)</strong>
	 * <p> {@code EquationParser test = new EquationParser("-z^2");}
	 * <p> {@code test.applyRule(new ComplexNumber(2, 0));}
	 * 
	 * <p>This equation parser takes a string representing a mathematical function in terms of 'z'. 
	 * The {@code applyRule()} method takes a {@code ComplexNumber} as an input and evaluates 
	 * the function with 'z' representing the input.
	 * 
	 * <p><strong> Currently in development, Prints out many lines for debugging purposes </strong>
	 * 
	 * <p><strong>Rules For Function String:</strong>
	 * <p><strong>1.</strong> Lower-case 'z' and 'i' are the only letters in the function expression.
	 * <p><strong>2.</strong> All exponents are non-negative single digit integers.
	 * <p><strong>3.</strong> Complex numbers are specified as follows {@code [} real {@code +} 
	 * imaginary{@code i]}
	 * <p><strong>4.</strong> Single component imaginary can be written without using format in 
	 * <strong>3</strong>, for example: {@code [0 + i]},  {@code 3} and {@code -2i} are all 
	 * acceptable. (Note that there is NO {@code *} between the constant and the 'i' for imaginary 
	 * components)
	 * <p><strong>5.</strong> The plus operator is {@code +}, minus is {@code -}, multiply 
	 * is {@code *} and divide is {@code /}.
	 * <p><strong>6.</strong> No parentheses are allowed
	 * 
	 * <p><strong>Examples of valid syntax:</strong>
	 * 
	 * <p> {@code "z + z ^ 2 + [4 -5i]*z^2 /- [3+48i]^3 - 5/z^2 "}
	 * <p> {@code "z^3 * -[3+ 67.55i] /-z - [45.4 - i]*-z^4 + [3.1-4.5i]"}
	 * 
	 */
	
	public EquationParser(String equation){
		setNewRule(equation);
	}
	
	public void setNewRule(String equation) {
//		System.out.println("1.  "+equation);
		// GETS RID OF WHITESPACE
		Scanner s = new Scanner(equation);
		equation = "";
		while (s.hasNext()) equation = equation + s.next();
		s.close();
		
//		System.out.println("2.  "+equation);
		ogEqn = equation;
		
		eqn = new LinkedList<>();
		{char[] copy = equation.toCharArray();
		for (int i = 0; i < copy.length; i++) {
			eqn.add(Character.toString(copy[i]));
		}}

		String currentToken;
		for (int i = 0;  i < eqn.size(); i++) {
			currentToken = eqn.get(i);
			if (currentToken.equals("[")) {
				i = convertComplexConstants(i);
			}
			else if (currentToken.equals("^")) {
				i = convertExponents(i);
			}
			else if (currentToken.equals("-")) {
				i = convertMinusSigns(i);
			}
			else if (currentToken.equals("+")) {
				i = convertPlusSigns(i);
			}
			else if (currentToken.equals("*")) {
				i = convertMultSigns(i);
			}
			else if (currentToken.equals("/")) {
				i = convertDivideSigns(i);
			}
			else try {
				Integer.parseInt(currentToken);
				i = convertIntoCmplxNum(i);
			}
			catch (NumberFormatException e){}
		}
		
		
		equation = "";
		for (int i = 0; i < eqn.size(); i++) {
			equation = equation + eqn.get(i);
		}
		
		
//		System.out.println("3.  "+eqn);
//		System.out.println("4.  "+equation);
		
		
		
		
		
		s = new Scanner(equation);
		s.useDelimiter("\\|");
		String term = null;
		rule = new LinkedList<>();
		LinkedList<String> stringChars = new LinkedList<>();
		while (s.hasNext()) {
			term = s.next();
			if (term.length() > 0) {
				stringChars = new LinkedList<>();
				for (int i = 0; i < term.length(); i++) {
					stringChars.add(Character.toString(term.charAt(i)));
				}
				rule.add(stringChars);
			}
		}
		
		// Cleans up weird bracket stuff
		for (int i = 0; i < rule.size(); i++) {
			if (rule.get(i).size() <= 1) {
				if (rule.get(i).get(0).equals("*")) {
					rule.get(i).remove(0);
					rule.get(i).add("$");
				} else if (rule.get(i).get(0).equals("~")) {
					rule.remove(i);
				}
				
			}
		}
		
		if (rule.get(0).get(0).equals("#")) {
			expandMinus(0);
		}
		for (int i = 1; i < rule.size(); i++) {
			String current = rule.get(i).get(0);
			String beforeCurrent = rule.get(i - 1).get(0);
			if (beforeCurrent.equals("$") || beforeCurrent.equals("@")) {
				if (current.equals("#")) {
					i = expandMinus(i);
				}
			}
		}
		
//		System.out.println("5.  "+rule);
		
		operators = new TreeMap<>();
		Iterator<LinkedList<String>> termsIter = rule.iterator();
		for (int i = 0; i < rule.size(); i++) {
			LinkedList<String> current = termsIter.next();
			if (current.size() == 1 && !current.get(0).equals("z")) {
				String operation = current.get(0);
				operators.put(i, operation);
			}
		}
		
		
		
		Set<Integer> opKeys = operators.keySet();
		Iterator<Integer> keyOp = opKeys.iterator();
		operatorIndices = new int[operators.size()];
		for (int i = operatorIndices.length - 1; i >= 0 ; i--) {
			operatorIndices[i] = keyOp.next();
		}
//		System.out.println(Arrays.toString(operatorIndices));
		
		plusOrMinusOrder = new LinkedList<>();
		for (int i = 0; i < operatorIndices.length; i++) {
			String operation = operators.get(operatorIndices[i]);
			if (operation.equals("#")) {
				plusOrMinusOrder.add(0, "#");
			} else if (operation.equals("%")) {
				plusOrMinusOrder.add(0, "%");
			}
		}
		
		eqn = null;
		System.gc();
		
		
		
		// REMOVE CODE BELOW IN FINAL VERSION
		//<><><><><><><><><><><><><><><><><><><><><><><><><><>
//		System.out.println("Testing version term uses '2 - i' to generate terms");
//		generateTermsList();
//		//<><><><><><><><><><><><><><><><><><><><><><><><><><>
//		
//		System.out.println("Terms = " + terms);
//		System.out.println("Operations occur at: " + operators);
//		
//		System.out.println("========== END OF CONSTRUCTOR OPERATIONS ==========");
		
		
	}
	
	private int convertComplexConstants(int openBracketIndex) {
		eqn.remove(openBracketIndex);
		String realNumber = "";
		int i = openBracketIndex;
		if (eqn.get(i).equals("-")) {  realNumber = "-"; eqn.remove(i);  }
		String currentString = eqn.get(i);
		while (!currentString.equals("+") && !currentString.equals("-")) {
			realNumber = realNumber + eqn.get(i);
			eqn.remove(i);
			currentString = eqn.get(i);
		}
		try { Double.parseDouble(realNumber); }
		catch (NumberFormatException e) {
			System.err.println("Invalid real component specified in complex number syntax");
		}
		
		
		
		String imaginaryNumber = "";
		if (eqn.get(i).equals("-")) imaginaryNumber = "-";
		eqn.remove(i);
		while (!eqn.get(i).equals("i")) {
			imaginaryNumber = imaginaryNumber + eqn.get(i);
			eqn.remove(i);
		}
		if (imaginaryNumber.length() == 0) {
			imaginaryNumber = "1";
		} else if (imaginaryNumber.equals("-")) {
			imaginaryNumber = "-1";
		}
		try { Double.parseDouble(imaginaryNumber); }
		catch (NumberFormatException e) {
			System.err.println("Invalid imaginary component specified in complex number syntax");
		}
		eqn.remove(i);
		eqn.remove(i);
		
		eqn.add(i, "|{" + realNumber + ":" + imaginaryNumber + "}|");
		return i;
	}
	
	private int convertExponents(int caretIndex) {
		
		String base = eqn.get(caretIndex -1);
		String exponentStr = eqn.get(caretIndex + 1);
		int exponent = 0;
		try { exponent = Integer.parseInt(exponentStr); }
		catch (NumberFormatException e) {
			System.err.println("Invalid exponent (Exponent must be a single digit and non-negative)");
		}
		String baseToken = base;
		base = "|~"+ baseToken;
		for (int j = 1; j < exponent; j++) {
			if (j == exponent - 1) base = base + "*" + baseToken;
			else base = base + "*" + baseToken;
		}
		base = base + "~|";
		eqn.remove(caretIndex -1);
		eqn.remove(caretIndex -1);
		eqn.remove(caretIndex -1);
		eqn.add(caretIndex -1, base);
		return caretIndex -1;
		
	}
	
	private int convertMinusSigns(int signIndex) {
		eqn.remove(signIndex);
		eqn.add(signIndex, "|#|");
		return signIndex;
	}
	
	private int convertPlusSigns(int signIndex) {
		eqn.remove(signIndex);
		eqn.add(signIndex, "|%|");
		return signIndex;
	}
	
	private int convertMultSigns(int signIndex) {
		eqn.remove(signIndex);
		eqn.add(signIndex, "|$|");
		return signIndex;
	}
	
	private int convertDivideSigns(int signIndex) {
		eqn.remove(signIndex);
		eqn.add(signIndex, "|@|");
		return signIndex;
	}
	
	private int convertIntoCmplxNum(int intIndex) {
		int intLength = 1;
		boolean complexComp = false;
		try {
			while (true) {
				String nextToken = eqn.get(intIndex + intLength);
				if (nextToken.equals("i")) {  complexComp = true;  break;  }
				if (!nextToken.equals(".")) Integer.parseInt(nextToken); 
				intLength++;
			}
		}
		catch (NumberFormatException e){}
		catch (IndexOutOfBoundsException e) {}
		
		if (complexComp) {
			String cmplxNum = "{0:";
			for (int i = 0; i < intLength; i++) {
				cmplxNum = cmplxNum + eqn.get(intIndex);
				eqn.remove(intIndex);
			}
			eqn.remove(intIndex);
			cmplxNum = cmplxNum + "}";
			eqn.add(intIndex, cmplxNum);
			return intIndex;
		}
		else {
			String realNum = "{";
			for (int i = 0; i < intLength; i++) {
				realNum = realNum + eqn.get(intIndex);
				eqn.remove(intIndex);
			}
			realNum = realNum + ":0}";
			eqn.add(intIndex, realNum);
			return intIndex;
		}
	}
	
	private int expandMinus(int minusIndex) {
		rule.get(minusIndex).clear();
		LinkedList<String> minusTerm = rule.get(minusIndex);
		minusTerm.add("{");
		minusTerm.add("-");
		minusTerm.add("1");
		minusTerm.add(":");
		minusTerm.add("0");
		minusTerm.add("}");
		LinkedList<String> times = new LinkedList<>();
		times.add("$");
		rule.add(minusIndex + 1, times);
		return minusIndex + 1;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	private void generateTermsList() {
		terms = new LinkedList<>();
		LinkedList<String> term = null;
		for (int i = 0; i < rule.size(); i++) {
			term = rule.get(i);
			if (term.get(0).equals("z") && term.size() == 1) 		terms.add(z);
			else if (term.size() == 1) 								terms.add(null); // we know this term is not a lone 'z' from above if
			else if (term.get(0).equals("{")) 						terms.add(convertToComplexNum(term));
			else if (term.get(0).equals("~")) 						terms.add(convertExponentialToComplexNum(term));
		}
	}
	
	public int findDepth(ComplexNumber zNum) {
		int depth = 0;
		ComplexNumber result = zNum;
		while (result.getMagnitude() <= 2.0 && depth < Mandelbrot.MAX_DEPTH) {
			result = applyRule(result);
			depth++;
		}
		return depth;
	}
	
	
	public ComplexNumber applyRule(ComplexNumber z) {
		this.z = z;
		generateTermsList();
		
//		System.out.println("Original Equation: "+ogEqn);
//		System.out.println("rule = "+rule);
//		System.out.println("terms = "+terms+"\n");
		for (int i = 0; i < operatorIndices.length; i ++) {
			int index = operatorIndices[i];
			String oper = operators.get(index);
			if (oper.equals("$")) {
				ComplexNumber performOp = terms.get(index - 1).multiply(terms.get(index + 1));
				terms.remove(index - 1);
				terms.remove(index - 1);
				terms.remove(index - 1);
				terms.add(index - 1, performOp);
//				System.out.println("Multiply "+terms);
			} else if (oper.equals("@")) {
				ComplexNumber performOp = terms.get(index - 1).divide(terms.get(index + 1));
				terms.remove(index - 1);
				terms.remove(index - 1);
				terms.remove(index - 1);
				terms.add(index - 1, performOp);
//				System.out.println("Divide "+terms);
			}
		}
		
		int i = 0;
		while (terms.size() > 1) {
			if (plusOrMinusOrder.get(i).equals("%")) {
				ComplexNumber sum = terms.get(0).add(terms.get(2));
				terms.remove(0);
				terms.remove(0);
				terms.remove(0);
				terms.add(0, sum);
				i++;
//				System.out.println("Add "+terms);
			}
			else if (plusOrMinusOrder.get(i).equals("#")) {
				ComplexNumber difference = terms.get(0).subtract(terms.get(2));
				terms.remove(0);
				terms.remove(0);
				terms.remove(0);
				terms.add(0, difference);
				i++;
//				System.out.println("Subtract "+terms);
			}
			else  {
				// no changes have been made, this isnt supposed to happen
				System.err.println("Something is off with your arithmatic, error in 'applyRule()' method");
				break;
			}
		}
		
//		System.out.println(terms.get(0));
		
		return terms.get(0);
	}
	
	
	
	
	
	private ComplexNumber convertToComplexNum(LinkedList<String> term) {
		String realStr = "";
		int i = 1;
		if (term.get(i).equals("{")) i++;
		for ( ; !term.get(i).equals(":"); i++) {
			realStr = realStr + term.get(i);
		}
		String imaginaryStr = "";
		i++;
		for (; !term.get(i).equals("}"); i++) {
			imaginaryStr = imaginaryStr + term.get(i);
		}
		
		
		try {  ComplexNumber result = new ComplexNumber(Double.parseDouble(realStr), Double.parseDouble(imaginaryStr)); return result;  }
		catch (NumberFormatException e) {  System.err.println("Unable to Parse Complex Number in 'convertToComplexNum' method");  }
		
		return null;
	}
	
	private ComplexNumber convertExponentialToComplexNum(LinkedList<String> expTerm) {
		if (expTerm.get(1).equals("z")) {
			ComplexNumber termResult = null;
			int expNum = (expTerm.size() - 1) / 2;
			if (expTerm.size() % 2 == 0) System.err.println("Something is off about a 'z' exponential term, Check 'convertExponential' method");
			ComplexNumber z_Copy = new ComplexNumber(z.getReal(), z.getImaginary());
			termResult = z_Copy.pow(expNum);
			return termResult;
		}
		else {
			if (!expTerm.get(1).equals("{")) System.err.println("Invalid exponent base, check 'convertExponentialToComplexNum' method");
			ComplexNumber base = convertToComplexNum(expTerm);
			ComplexNumber baseToken = new ComplexNumber(base.getReal(), base.getImaginary());
			for (int i = 2; i < expTerm.size(); i++) {
				if (expTerm.get(i).equals("*")) {
					base = base.multiply(baseToken);
				}
			}
			return base;
		}
	}
	
	
	
	
	
}


