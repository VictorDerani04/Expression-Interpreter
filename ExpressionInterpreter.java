
public class ExpressionInterpreter {
    public static void main(String[] args) {
        SinglyLinkedList<String> tokens = Tokenizer.tokenize(args[0]);
        ExpressionInterpreter interpreter = new ExpressionInterpreter();

        // 1) Build the expression Tree From the Tokens (requires a Node class)
        Node root = interpreter.buildExpressionTree(tokens);

        System.out.print("Postfix: ");
        // 2) Print the post order travesal (postfix notation)
        printPostOrder(root);
        System.out.print("\n");

        System.out.print("Infix: ");
        // 3) Print the inorder travesal (infix notation) with additional parentheses
        printInOrder(root);
        System.out.print("\n");

        // 4) Solve the expression as much as possible, printing out the unbound variables.
        root = solveAsMuchAsPossible(root);
        
        System.out.print("Solved: ");
        // 5) Print the solved expression.
        printInOrder(root);
        System.out.print("\n");
    }

    public Node buildExpressionTree(SinglyLinkedList<String> tokens) {
        Stack<Node> expressionStack = new ArrayStack<>();
        Stack<String> operatorStack = new ArrayStack<>();

        operatorStack.push("(");
        tokens.addLast(")");

        while (!tokens.isEmpty()) {
            String token = tokens.removeFirst();

            if (operand(token)) {
                expressionStack.push(new Node(token));
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (operator(token)) {
                while (!operatorStack.isEmpty() && !operatorStack.top().equals("(") &&
                        level(operatorStack.top()) >= level(token)) {
                    popOperatorStack(expressionStack, operatorStack);
                }
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.top().equals("(")) {
                    popOperatorStack(expressionStack, operatorStack);
                }
                operatorStack.pop();
            }
        }

        return expressionStack.pop();
    }

    public boolean operand(String token) {
        return !operator(token) && !token.equals("(") && !token.equals(")");
    }

    public boolean operator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    public int level(String operator) {
        switch (operator) {
            case "^":
                return 3;  // Highest precedence
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    public static void popOperatorStack(Stack<Node> expressionStack, Stack<String> operatorStack){
        Node right = expressionStack.pop();
        Node left = expressionStack.pop();
        Node newNode = new Node(operatorStack.pop(), left, right);
        expressionStack.push(newNode);
    }

    public static void printPostOrder(Node root) {
        // TODO - fill this in
        if(root==null){
            return;
        }
        printPostOrder(root.leftChild);
        printPostOrder(root.rightChild);
        System.out.print(root.element + " ");
    }
    
    public static void printInOrder(Node root) {
        // TODO - fill this in
        if(root==null){
            return;
        }
         boolean operator = root.element.equals("+") || root.element.equals("-") || root.element.equals("*") ||
                 root.element.equals("/") || root.element.equals("^");

        if(operator){
            System.out.print("(");
        }
        printInOrder(root.leftChild);

        if(operator){
            System.out.print(" " + root.element + " ");
        }
        else{
            System.out.print(root.element);
        }

        printInOrder(root.rightChild);

        if(operator){
            System.out.print(")");
        }
    }

    public static Node solveAsMuchAsPossible(Node root) {
        if (root == null) return null;

        boolean isOperator = root.element.equals("+") || root.element.equals("-") ||
                root.element.equals("*") || root.element.equals("/") || root.element.equals("^");

        if (!isOperator) {
            try {
                Double.parseDouble(root.element);
                return root;
            } catch (NumberFormatException e) {
                System.out.println("Unbound variable: " + root.element);
                return root;
            }
        }

        Node leftChild = solveAsMuchAsPossible(root.leftChild);
        Node rightChild = solveAsMuchAsPossible(root.rightChild);

        if (number(leftChild.element) && number(rightChild.element)) {
            double leftChildValue = Double.parseDouble(leftChild.element);
            double rightChildValue = Double.parseDouble(rightChild.element);
            double result;

            switch (root.element) {
                case "+":
                    result = leftChildValue + rightChildValue;
                    break;
                case "-":
                    result = leftChildValue - rightChildValue;
                    break;
                case "*":
                    result = leftChildValue * rightChildValue;
                    break;
                case "/":
                    if (rightChildValue == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    result = leftChildValue / rightChildValue;
                    break;
                case "^":
                    result = Math.pow(leftChildValue, rightChildValue);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + root.element);
            }

            return new Node(String.valueOf(result));
        }

        root.leftChild = leftChild;
        root.rightChild = rightChild;
        return root;
    }

    public static boolean number(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}