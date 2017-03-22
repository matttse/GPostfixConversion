import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;

public class GeneralPostfixConversion {

	public static void main(String[] args) {
		//while there is an expression to read
		//reads infix expression from input file
		//store it in array of strings named tokens
		//prints the converted postfix expression
		try {
			File file = new File("C:/infixExpressions.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			stringBuffer.trimToSize();

			String testout = "";
	
			
			System.out.println(testout);
			fileReader.close();
			System.out.println("Contents of file:");
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	//end main method
	
	//infix only assuming integers no doubles
	//TODO toPostFix
	public String toPostFix(String[] tokens) {
		//the formal parameter tokens in an array of tokens
		//of an infix expression
		
		//the corresponding postfix expresison
		return null;
		
	}//end toPostFix
	//TODO printStack
	public void printStack(LinkedStack<String> st) {
		//the formal parameter st is a linked list-based stack
		//prints all elements of the stack beginning with the
		//stack top element
		//space between 2 consecutive elements
	}//end printStack
	public static boolean isOperand(String s) {
		return !(s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*") || s.equals("(") || s.equals(")"));
	}
	
	public static boolean isNumber(String s){
		try {
			Integer.parseInt(s.trim());
		} catch (Exception e){
			return false;
		}
		return true;
	}
	
	public static String operationCombine(Stack<String> operatorStack, Stack<String> operandStack, boolean reduce){
		String operator = operatorStack.pop();
		String rightOperand = operandStack.pop();
		String leftOperand = operandStack.pop();
		if (reduce && isNumber(rightOperand) && isNumber(leftOperand)){
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

	public static String infixToPrefixConvert(String infix, boolean reduce) {
		Stack<String> operandStack = new Stack<String>();
		Stack<String> operatorStack = new Stack<String>();

		StringTokenizer tokenizer = new StringTokenizer(infix);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (isOperand(token)) {
				operandStack.push(token);
			}

			else if (token.equals("(") || operatorStack.isEmpty()
					|| rank(token) > rank(operatorStack.peek())) {
				operatorStack.push(token);
			}

			else if (token.equals(")")) {
				while (!operatorStack.peek().equals("(")) {
					operandStack.push(operationCombine(operatorStack, operandStack,reduce));
				}
				operatorStack.pop();
			}
			
			else if( rank(token) <= rank(operatorStack.peek())){
				while(!operatorStack.isEmpty() && rank(token) <= rank(operatorStack.peek())){
					operandStack.push(operationCombine(operatorStack, operandStack,reduce));
				}
				operatorStack.push(token);
			}
		}
		while( !operatorStack.isEmpty() ) {
			operandStack.push(operationCombine(operatorStack, operandStack,reduce));
		}
		return (operandStack.peek());
	}
	

}