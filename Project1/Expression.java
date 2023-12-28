// Starter code for Project 1

// sxm190157
// note: the code has an error with package dsa. Commenting out "package dsa" made my code work just fine. Not sure why I am having this error.
package dsa;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/** Class to store a node of expression tree
    For each internal node, element contains a binary operator
    List of operators: +|*|-|/|%|^
    Other tokens: (|)
    Each leaf node contains an operand (long integer)
*/

public class Expression {
    public enum TokenType {  // NIL is a special token that can be used to mark bottom of stack
	PLUS, TIMES, MINUS, DIV, MOD, POWER, OPEN, CLOSE, NIL, NUMBER
    }
    
    public static class Token {
	TokenType token;
	int priority; // for precedence of operator
	Long number;  // used to store number of token = NUMBER
	String string;

	Token(TokenType op, int pri, String tok) {
	    token = op;
	    priority = pri;
	    number = null;
	    string = tok;
	}

	// Constructor for number.  To be called when other options have been exhausted.
	Token(String tok) {
	    token = TokenType.NUMBER;
	    number = Long.parseLong(tok);
	    string = tok;
	}
	
	boolean isOperand() { return token == TokenType.NUMBER; }

	public long getValue() {
	    return isOperand() ? number : 0;
	}

	public String toString() { return string; }
    }

    Token element;
    Expression left, right;

    // Create token corresponding to a string
    // tok is "+" | "*" | "-" | "/" | "%" | "^" | "(" | ")"| NUMBER
    // NUMBER is either "0" or "[-]?[1-9][0-9]*
    static Token getToken(String tok) {  // To do
	Token result;
	switch(tok) {
	case "+":
	    result = new Token(TokenType.PLUS, 1, tok);  // modify if priority of "+" is not 1
	    break;
	    // Complete rest of this method
	case "*":
	    result = new Token(TokenType.TIMES, 2, tok);  // priority of "*" is 2
	    break;	    
	case "-":
	    result = new Token(TokenType.MINUS, 1, tok);  // priority of "-" is 1
	    break;	
	case "/":
	    result = new Token(TokenType.DIV, 2, tok);  // priority of "/" is 2
	    break;
	case "%":
	    result = new Token(TokenType.MOD, 2, tok);  // priority of "%" is 2
	    break;	
	case "^":
	    result = new Token(TokenType.POWER, 3, tok);  // priority of "^" is 3
	    break;	    
	case "(":
	    result = new Token(TokenType.OPEN, 0, tok);  // priority of "(" is 0
	    break;
	case ")":
	    result = new Token(TokenType.CLOSE, 0, tok);  // priority of ")" is 0
	    break;	    
	default:
	    result = new Token(tok);
	    break;
	}
	return result;
    }
    
    private Expression() {
	element = null;
    }
    
    private Expression(Token oper, Expression left, Expression right) {
	this.element = oper;
	this.left = left;
	this.right = right;
    }

    private Expression(Token num) {
	this.element = num;
	this.left = null;
	this.right = null;
    }

/**
 * Given list of tokens corresponding to an infix expression,
 * return the corresponding expression tree.
 *
 * @param exp List of tokens representing the infix expression
 * @return Expression tree representing the infix expression
 */
    public static Expression infixToExpression(List<Token> exp) {  // To do
	    // two stacks created below -> one for operands, one for operators
	    Deque<Expression> operands = new ArrayDeque<>(); //arraydeque of operands
	    Deque<Token> operators = new ArrayDeque<>(); //arraydeque of operators (+,-,*,^,/,%,(,))
	    
	    for (Token token:exp){
	        if(token.isOperand()){
	            operands.push(new Expression(token)); //if token is operand, push a new expression node with the operand onto operand stack
	        }
	        else if(token.token == TokenType.OPEN){
	            operators.push(token); //if token is open parenthesis push it onto operator stack
	        }
	        else if(token.token == TokenType.CLOSE){ // if token is close parenthesis, pop operators from stack until open parenthesis is found
                while (!operators.isEmpty() && operators.peek().token != TokenType.OPEN) {
                    Expression right = operands.pop();
                    Expression left = operands.pop();
                    operands.push(new Expression(operators.pop(), left, right));
                }
                operators.pop(); // Remove the open parenthesis
	        }
	        else{ // if token is operator, pop operators from stack till stack is empty or top operator has lower priority
	            while(!operators.isEmpty() && token.priority <= operators.peek().priority){
	                Expression right = operands.pop();
	                Expression left = operands.pop();
	                operands.push(new Expression(operators.pop(), left, right));
	            }
	            operators.push(token); // push current operator to stack
	        }
	        
	    }
	    
	    while(!operators.isEmpty()){ //pop remaining operators in stack to build expression tree
	        Expression right = operands.pop();
	        Expression left = operands.pop();
	        operands.push(new Expression(operators.pop(),left,right));
	    }
	    
	    return operands.pop();
    }

/**
 * Given list of tokens corresponding to an infix expression,
 * return the equivalent postfix expression as list of tokens.
 *
 * @param exp List of tokens representing infix expression
 * @return List of tokens representing postfix expression
 */
    public static List<Token> infixToPostfix(List<Token> exp) {  // To do
	    // create list for postfix expression and stack for operators
	    List<Token> postfix = new LinkedList<>();
	    Deque<Token> stack = new ArrayDeque<>();
	    
	    for (Token token:exp){
	        if(token.isOperand()){
	            postfix.add(token); // if token is operand, add to the postfix expression
	        }
	        else if(token.token == TokenType.OPEN){
	            stack.push(token); // if token is open parenthesis, push it onto stack
	        }
	        else if(token.token == TokenType.CLOSE){ // if token is close parenthesis, pop operators from stack until open parenthesis is found
	            while(!stack.isEmpty() && stack.peek().token != TokenType.OPEN){
	                postfix.add(stack.pop());
	            }
	            stack.pop(); //remove open parenthesis
	        }
	        else{ //if token is operator, pop operators from stack till stack is empty or top operator has lower priority
	            while(!stack.isEmpty() && token.priority <= stack.peek().priority){
	                postfix.add(stack.pop());
	            }
	            stack.push(token);
	        }
	       
	    }
	    
	    while(!stack.isEmpty()){//pop remaining operators in stack and put into postfix expression
	        postfix.add(stack.pop());
	    }	    
	    
	    return postfix;
    }

/**
 * Given postfix expression, evaluate it and return its value.
 *
 * @param exp List of tokens representing the postfix expression
 * @return Result of evaluating the postfix expression
 */
    public static long evaluatePostfix(List<Token> exp) {  // To do
	    //stack created for operands
	    Deque<Long> stack = new ArrayDeque<>();
	    
	    for (Token token : exp){
	        if(token.isOperand()){ // if token is operand, push its value to stack
	            stack.push(token.getValue());
	        }
	        else{ // if token is operator, pop operands from stack and do the respective calculation. Push result of the calculation back to the stack
	            long right = stack.pop();
	            long left = stack.pop();
	            switch(token.token){
	                case PLUS:
	                    stack.push(left + right);
	                    break;
	                case TIMES:
	                    stack.push(left * right);
	                    break;
	                case MINUS:
	                    stack.push(left - right);
	                    break;
	                case DIV:
	                    stack.push(left / right);
	                    break;
	                case MOD:
	                    stack.push(left % right);
	                    break;
	                case POWER:
	                    stack.push((long) Math.pow(left, right));
	                    break;
	            }
	        }
	    }
	    
	    return stack.pop();
    }

/**
 * Given expression tree, evaluate it and return its value.
 *
 * @param tree Expression tree to be evaluated
 * @return Result of evaluating the expression tree
 */
    public static long evaluateExpression(Expression tree) {  // To do
        // if tree root is an operand, return it's value
        if(tree.element.isOperand()){
           return tree.element.getValue(); 
        }
        
        // if root is operator, check left and right subtrees and apply the respective operator for the calculation 
        long leftValue = evaluateExpression(tree.left);
        long rightValue = evaluateExpression(tree.right);
        switch(tree.element.token){
	           case PLUS:
	               return leftValue + rightValue;
	           case TIMES:
	               return leftValue * rightValue;
	           case MINUS:
	               return leftValue - rightValue;
	           case DIV:
	               return leftValue / rightValue;
	           case MOD:
	               return leftValue % rightValue;
	           case POWER:
	               return (long) Math.pow(leftValue, rightValue);
	            default:
	                return 0;
                }
    }

    // sample main program for testing
    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;
	
	System.out.println("Enter expression you want to calculate. Put space between each operator and operand. Example: 4 + 5 * 8 ^ 2 * ( 8 + 5 )");
	
	if (args.length > 0) {
	    File inputFile = new File(args[0]);
	    in = new Scanner(inputFile);
	} else {
	    in = new Scanner(System.in);
	}

	int count = 0;
	while(in.hasNext()) {
	    String s = in.nextLine();
	    List<Token> infix = new LinkedList<>();
	    Scanner sscan = new Scanner(s);
	    int len = 0;
	    while(sscan.hasNext()) {
		infix.add(getToken(sscan.next()));
		len++;
	    }
	    if(len > 0) {
		count++;
		System.out.println("Expression number: " + count);
		System.out.println("Infix expression: " + infix);
		Expression exp = infixToExpression(infix);
		List<Token> post = infixToPostfix(infix);
		System.out.println("Postfix expression: " + post);
		long pval = evaluatePostfix(post);
		long eval = evaluateExpression(exp);
		System.out.println("Postfix eval: " + pval + " Exp eval: " + eval + "\n");
	    }
	}
    }
}
