
package nz.co.lost.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nz.co.lost.jlox.TokenType.*;


/**
Scanner produces list of tokens from program source.
*/
class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    /**
    Scan through entire source, not stopping on errors.
    */
    List<Token> scanTokens() {
        while (! isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
    Append a Token to `tokens` without a literal value.
    */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
    Append a Token with a literal value.
    */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
    Consume and return next character in source.
    */
    private char advance() {
        return source.charAt(current++);
    }

    /**
    Have we consumed all of the characters in source?
    */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
    Consume the next character only if it's what we're looking for.
    */
    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    /**
    Look ahead at next character, but do not consume.
    */
    private char peek() {
        if (isAtEnd())
            return '\0';

        return source.charAt(current);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // Single character
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;

            // One or two characters
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // Comment or division?
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && ! isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;

            // Skip whitespace
            case ' ':
            case '\r':
            case 't':
                break;

            // Newline
            case '\n':
                line++;
                break;

            // String literal
            case '"':
                string();
                break;

            default:
                Lox.error(line, "Unexpected character.");
        }
    }

    private void string() {
        // Advance to end of string, allowing new-lines.
        while (peek() != '"' && ! isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        // End of file found?
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // Consume closing quote-marks
        advance();

        // Strip quote-marks and create literal
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }
}
