import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GeneralPostfixConversion {
	public LinkedStack<String> expressionStack = new LinkedStack<String>();
/*
 * Example File Input:  tokens must be separated by one or more spaces
( ( ( A + B ) *C ) - ( ( D - E ) * ( F + G ) ) )
3 * X + ( Y - 12 ) - Z 
 * 
 */
	public static void main(String[] args) {
		GeneralPostfixConversion postFixExp = new GeneralPostfixConversion();		
		//throws IOException
		try {
			File file = new File("C:/infixExpressions.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			String[] tokenBufferLine;//stores each line expression
			String[] tokens;//tokens expression 			
			//while there is an expression to read
			//reads infix expression from input file
			while ((line = bufferedReader.readLine()) != null) {
				if (isBalanced(line)) {
					stringBuffer.append(line);
					stringBuffer.append(";\n");	
				} else {
					System.out.println(line);
					System.out.println("Not Balanced, fix infix expression\n");
				}

			}//end while expressions
			//cleanup
			stringBuffer.trimToSize();
			fileReader.close();
			//instantiate size of array of strings
			tokenBufferLine = stringBuffer.toString().split(";");
			for (int i = 0; i < tokenBufferLine.length; i++) {
				tokens = tokenBufferLine[i].replaceAll("\\s+", " ").split(" ");
				//only process if there are balanced legal stored expressions
				if (tokens.length > 0) {
					postFixExp.toPostFix(tokens);				
				}	
			}

			System.out.println("Contents of file:");		
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}//end trycatch
		
	}
	//end main method
	/*
	 * @Method printStack
	 * @precondition: stack
	*/
	public void printStack(LinkedStack<String> stack) {//the formal parameter st is a linked list-based stack
		//prints all elements of the stack beginning with the
		while (!stack.isEmpty()) {
			System.out.print(stack.pop());
		}
		//stack top element
		//space between 2 consecutive elements
		System.out.println("\n---------------");
	}//end printStack
	/*
	 * @Method toPostFix
	 * @precondition: infix only assuming integers no doubles
	 * the formal parameter tokens in an array of tokens
	 * of an infix expression
	*/
	public String toPostFix(String[] tokens) {
		//A B + C * D E - F G + * -
		//3 X * Y 12 - + Z -
		// Meaningful names for characters
		String C_LEFT_PARENS  = "(";
		String C_RIGHT_PARENS = ")";
		LinkedStack<String> operandStack = new LinkedStack<String>();
		LinkedStack<String> operatorStack = new LinkedStack<String>();
		LinkedStack<String> expStack = new LinkedStack<String>();
		LinkedStack<String> cloneExpStack = new LinkedStack<String>();
		String expression = "";
		GeneralPostfixConversion postFixed = new GeneralPostfixConversion();
		
		for (int x = 0; x < tokens.length; x++) {//convert an infix expression to a postfix expression, general case	
			String token = tokens[x];
			if (token.equals(C_LEFT_PARENS)) {//token is left parens
				if (validAlphaNum(token) == true) {
					operandStack.push(token);
				}								
			} else if (token.equals(C_RIGHT_PARENS)) {//token is a right parens or is empty to start next expression
				operandStack.push(subOpCombine(operatorStack, operandStack));
			} else if (validOperator(token) == true) {//token is an operator
				operatorStack.push(token);				
			} else {//default, assume its an operand
				operandStack.push(token);	
			} 
		}	
		
		while(!operandStack.isEmpty()) {//rebuild subExpression to expression string
			//initialize temp strings
			String postFixExp = "", rOperator = "", lOperator = "";
			if (operatorStack.isEmpty()){//if there were no other operations to perform
				postFixExp += operandStack.pop();
				expStack.push(postFixExp);
			} else {//combine subOperations into string
				String right = operandStack.pop();
				String left = operandStack.pop();
				if (operatorStack.size() == 1) {//last operation
					rOperator = operatorStack.pop();
					postFixExp += (left + " " + right + " " + rOperator + " ");
				} else {//recombine in appropriate postfix notation
					rOperator = operatorStack.pop();
					lOperator = operatorStack.pop();		
					postFixExp += (left + " " + lOperator + " " + right + " " + rOperator + " ");
				}
				//push expression back into a stack to be consumed by printStack
				expStack.push(postFixExp);
			}
		}
		//clone stack to store as string
		cloneExpStack = expStack.clone();
		while (!cloneExpStack.isEmpty()){
			expression += cloneExpStack.pop();
		}
		//send to print stack
		postFixed.printStack(expStack);

		//the corresponding postfix expresison
		return(expression);	
			
	}//end toPostFix
	
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
	public static String subOpCombine(LinkedStack<String> operatorStack, LinkedStack<String> operandStack){
		String operator = operatorStack.pop();
		String rightOperand = operandStack.pop();
		String leftOperand = operandStack.pop();
		//Assume Integers ONLY as stated by requirements
		if (validNum(leftOperand, 0) == true && validNum(rightOperand, 0) == true){
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
//		String operand = "( " + leftOperand + " "+ rightOperand + " " + operator +" )";
		String operand = leftOperand + " " + rightOperand + " " + operator;
		return operand;
	}
	
	/*
	 * @Name: validOperator
	 * 
	 * @Function/Purpose: Validates tokens
	 * 
	 * @Parameters:
	 * 		{vc} validOperator
	 * 
	 * @Additionl Comments: 
	 * 
	 * @Return {boolean} true/false
	 */
	public static boolean validOperator(String validOperator) {
		return (validOperator.equals("+") || validOperator.equals("-")
				|| validOperator.equals("/") || validOperator.equals("*"));
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
	 * @Name: validNum
	 * 
	 * @Function/Purpose: Validates number input
	 * 
	 * @Parameters:
	 * 		{vc} String input
	 * 		{i4} flag
	 * @Additionl Comments: called from validate method
	 * 
	 * @Return true/false based on valid 
	 */
	public static boolean validNum(String userInput, int flag) {
		boolean validInput = false;

		switch (flag) {
		case 0:
			try {

				int chkInt = Integer.parseInt(userInput);
				validInput = true;

			} catch (NumberFormatException nfe) {

				validInput = false;

			}
			break;

		case 1:

			try {

				Float chkFloat = Float.parseFloat(userInput);
				validInput = true;

			} catch (NumberFormatException nfe) {

				validInput = false;

			}
			break;

		case 2:

			try {

				Double chkDouble = Double.parseDouble(userInput);
				validInput = true;

			} catch (NumberFormatException nfe) {

				validInput = false;

			}
			break;

		default:
			validInput = false;
			break;
		}

		return validInput;

	}// end method

	public static boolean isBalanced(String expression)
	// Postcondition: A true return value indicates that the parentheses in the
	// given expression are balanced. Otherwise the return value is false.
	// Note that characters other than ( ) { } and [ ] are ignored.
	{
	   // Meaningful names for characters
	   final char LEFT_NORMAL  = '(';
	   final char RIGHT_NORMAL = ')';
	   final char LEFT_CURLY   = '{';
	   final char RIGHT_CURLY  = '}';
	   final char LEFT_SQUARE  = '[';
	   final char RIGHT_SQUARE = ']';
	   
	   Stack<Character> store = new Stack<Character>( ); // Stores parens
	   int i;                              // An index into the string
	   boolean failed = false;             // Change to true for a mismatch
	   
	   for (i = 0; !failed && (i < expression.length( )); i++)
	   {
	      switch (expression.charAt(i))
	      {	//if any of these cases are open
	         case LEFT_NORMAL:
	         case LEFT_CURLY:
	         case LEFT_SQUARE: 
	            store.push(expression.charAt(i));
	            break;
	         case RIGHT_NORMAL:
	            if (store.isEmpty( ) || (store.pop( ) != LEFT_NORMAL))
	               failed = true;
	            break;
	         case RIGHT_CURLY:
	            if (store.isEmpty( ) || (store.pop( ) != LEFT_CURLY))
	               failed = true;
	            break;
	         case RIGHT_SQUARE:
	            if (store.isEmpty( ) || (store.pop( ) != LEFT_SQUARE))
	               failed = true;
	            break;
	      }
	   }
	   
	   return (store.isEmpty( ) && !failed);
	}
	


}