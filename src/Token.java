
package jlox;

/**
Tokens output from the scanner.
*/
class Token {
    final TokenType type;   // enum eg. PLUS, MINUS, STRING
    final String lexeme;    // Original string
    final Object literal;   // Converted literal or null
    final int line;         // Source line

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        String string = "<" + type;
        if (literal != null) {
            string += ":" + literal;
        }
        if (type == TokenType.IDENTIFIER) {
            string += ":" + lexeme;
        }
        string += ">";
        return string;
    }
}
