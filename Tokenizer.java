
public class Tokenizer {
    public static SinglyLinkedList<String> tokenize(String expression) {
        SinglyLinkedList<String> tokens = new SinglyLinkedList<>();
        StringBuilder numberBuffer = new StringBuilder();
        StringBuilder variableBuffer = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            // Ignore whitespaces
             if (Character.isWhitespace(currentChar)) {
                if (numberBuffer.length() > 0) {
                    tokens.addLast(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }
                if (variableBuffer.length() > 0) {
                    tokens.addLast(variableBuffer.toString());
                    variableBuffer.setLength(0);
                }
                continue;
            }

            // If it's a digit, add to number buffer
            if (Character.isDigit(currentChar) || currentChar == '.') {
                if (variableBuffer.length() > 0) {  // Add variable buffer first if it exists
                    tokens.addLast(variableBuffer.toString());
                    variableBuffer.setLength(0);
                }
                numberBuffer.append(currentChar);
            }
            // If it's a letter, treat it as part of a variable name
            else if (Character.isLetter(currentChar)) {
                if (numberBuffer.length() > 0) {  // Add number buffer first if it exists
                    tokens.addLast(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }
                variableBuffer.append(currentChar);
            } else {
                // If a number was being built, add it to tokens
                if (numberBuffer.length() > 0) {
                    tokens.addLast(numberBuffer.toString());
                    numberBuffer.setLength(0);  // Clear the buffer
                }

                if (variableBuffer.length() > 0) {
                    tokens.addLast(variableBuffer.toString());
                    variableBuffer.setLength(0);  // Clear the buffer
                }

                // Add operators or parentheses directly to tokens
                if (isOperator(currentChar) || currentChar == '(' || currentChar == ')') {
                    tokens.addLast(String.valueOf(currentChar));
                }
            }
        }

        // Add the last number if still in the buffer
        if (numberBuffer.length() > 0) {
            tokens.addLast(numberBuffer.toString());
        }

        if (variableBuffer.length() > 0) {
            tokens.addLast(variableBuffer.toString());
        }

        return tokens;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
}