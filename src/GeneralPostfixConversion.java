import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GeneralPostfixConversion {

	public static void main(String[] args) {
		//throws IOException
		try {
			File file = new File("C:/infixExpressions.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			ArrayList<String> tokenBuffer =  new ArrayList<String>();//temporary storage
			String[] tokens;//store it in array of strings named tokens 
			String testout = "";
			
			
			//while there is an expression to read
			//reads infix expression from input file
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}//end while expressions
			//cleanup
			stringBuffer.trimToSize();
			fileReader.close();

			//loop through raw input
			for (int i = 0; i < stringBuffer.length(); i++) {				
				if (stringBuffer.charAt(i) != 32) {//check for ascii space
					tokenBuffer.add(String.valueOf(stringBuffer.charAt(i))); 				
				}
			}//end for loop
			
			//instantiate size of array of strings
			tokens = new String[tokenBuffer.size()];
			//loop through arraylist
			for (int i = 0; i < tokenBuffer.size(); i++) {
				tokens[i] = String.valueOf(tokenBuffer.get(i)); 				
//				System.out.println(tokens[i]);
			}//end for loop	
			System.out.print(toPostFix(tokens));
			
			System.out.println("Contents of file:");		
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}//end trycatch
		
		//TODO prints the converted postfix expression

	}
	//end main method
	
	/*
	 * @Method toPostFix
	 * @precondition: infix only assuming integers no doubles
	 * the formal parameter tokens in an array of tokens
	 * of an infix expression
	*/
	public static String toPostFix(String[] tokens) {
		// Meaningful names for characters
		final char C_LEFT_PARENS  = '(';
		final char C_RIGHT_PARENS = ')';
		LinkedStack<String> operandStack = new LinkedStack<String>();
		LinkedStack<String> operatorStack = new LinkedStack<String>();
		
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			//TODO fix logic to match Pseudocode algo requirements
			if (validOp(token)) {
				operandStack.push(token);
			}

			else if (token.equals(C_LEFT_PARENS) || operatorStack.isEmpty()
					|| rank(token) > rank(operatorStack.peek())) {
				operatorStack.push(token);
			}

			else if (token.equals(C_RIGHT_PARENS)) {
				while (!operatorStack.peek().equals(C_LEFT_PARENS)) {
					operandStack.push(operationCombine(operatorStack, operandStack));
				}
				operatorStack.pop();
			}
			
			else if( rank(token) <= rank(operatorStack.peek())){
				while(!operatorStack.isEmpty() && rank(token) <= rank(operatorStack.peek())){
					operandStack.push(operationCombine(operatorStack, operandStack));
				}
				operatorStack.push(token);
			}
		}

		while( !operatorStack.isEmpty() ) {
			operandStack.push(operationCombine(operatorStack, operandStack));
		}
		//the corresponding postfix expresison
		return (operandStack.peek());		
	}//end toPostFix
	
	//TODO printStack
	public void printStack(LinkedStack<String> st) {
		//the formal parameter st is a linked list-based stack
		//prints all elements of the stack beginning with the
		//stack top element
		//space between 2 consecutive elements
	}//end printStack
	
	/*
	 * @Name: isOperand
	 * 
	 * @Function/Purpose: Validates tokens
	 * 
	 * @Parameters:
	 * 		{vc} validOperand
	 * 
	 * @Additionl Comments: 
	 * 
	 * @Return {boolean} true/false
	 */
	public static boolean validOp(String validOperand) {
		return !(validOperand.equals("+") || validOperand.equals("-")
				|| validOperand.equals("/") || validOperand.equals("*") 
				|| validOperand.equals("(") || validOperand.equals(")"));
	}//end isOperand
	
	/*
	 * @Name: validAlphaNum
	 * 
	 * @Function/Purpose: Validates alphanumeric input
	 * 
	 * @Parameters: {vc} String input
	 * 
	 * @Return true/false based on valid 
	 */
	public static boolean validAlphaNum(String validNumber) {
		boolean validInput = false;

		for (int charCnt = 0; validNumber.length() > charCnt; charCnt++) {

			// iterate over each char in input
			char charac = validNumber.charAt(charCnt);

			// look for valid alphabetical values (a-z/A-Z/0-9)
			if ((charac > 64 && charac < 91) || (charac > 96 && charac < 123) || (charac > 47 && charac < 58)) {

				// Valid case
				validInput = true;

			} else {

				// Invalid char a-z/A-Z
				validInput = false;

			}

		} // end for loop


		return validInput;

	}// end method	
	/*
	 * @Name: operationCombine
	 * 
	 * @Function/Purpose: Validates tokens
	 * 
	 * @Parameters:
	 * 		{vc} validNumber
	 * 
	 * @Additionl Comments: 
	 * 
	 * @Return {boolean} true/false
	 */
	public static String operationCombine(LinkedStack<String> operatorStack, LinkedStack<String> operandStack){
		String operator = operatorStack.pop();
		String rightOperand = operandStack.pop();
		String leftOperand = operandStack.pop();
		if (validAlphaNum(rightOperand) && validAlphaNum(leftOperand)){
			int left = Integer.parseInt(leftOperand);
			int right = Integer.parseInt(rightOperand);
			int result = 0;
			if (operator.equals("+")){
				result = left + right;
			}else if (operator.equals("-")){
				result = left - right;
			}else if (operator.equals("*")){
				result = left * right;
			}else if (operator.equals("/")){
				result = left / right;
			}
			return "" + result;
			
		}
		String operand = "(" + operator + " " + leftOperand + " "+ rightOperand + ")";
		return operand;
	}

	public static int rank(String s) {
		if (s.equals("+") || s.equals("-"))
			return 1;
		else if (s.equals("/") || s.equals("*"))
			return 2;
		else
			return 0;
	}

	


}