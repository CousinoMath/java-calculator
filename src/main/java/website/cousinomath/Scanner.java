package website.cousinomath;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;

class Scanner {
    private final TreeSet<String> constants =
        new TreeSet<String>(Arrays.asList("e", "pi"));
    private final TreeSet<String> functions =
        new TreeSet<String>(
                Arrays.asList("acos", "asin", "atan", "cos", "exp", "log",
                    "sin", "tan")); 
    private final String source;
    private final int length;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    
    public Scanner(String source) {
        this.source = source.trim();
        this.length = this.source.length();
    }

    public Result<List<Token>, String> lex() {
        if (this.current == 0) {
            while (this.current < this.length) {
                this.skipWhitespace();
                this.start = this.current;
                this.advance();
                Result<Token, String> result = this.nextToken();
                if (result.isErr()) {
                    return new Err<>(result.unwrapErr());
                }
                this.tokens.add(result.unwrap());
            }
            this.tokens.add(new Token(TokenType.EOI, "♣"));
        }
        return new Ok<>(this.tokens);
    }

    private void advance() {
        if (this.current < this.length) {
            this.current++;
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(this.source.charAt(this.current))
                && this.current < this.length) {
            this.advance();
        }
    }

    private Result<Token, String> nextToken() {
        char start = this.source.charAt(this.start);
        if (Character.isDigit(start)) {
            return this.parseNumber();
        }
        if (Character.isAlphabetic(start)) {
            return this.parseIdentifier();
        }
        switch (start) {
            case '+': return new Ok<>(new Token(TokenType.PLUS, "+"));
            case '-': return new Ok<>(new Token(TokenType.DASH, "-"));
            case '*': return new Ok<>(new Token(TokenType.STAR, "*"));
            case '/': return new Ok<>(new Token(TokenType.SLASH, "/"));
            case '^': return new Ok<>(new Token(TokenType.CARET, "^"));
            case '=': return new Ok<>(new Token(TokenType.EQUALS, "="));
            case '(': return new Ok<>(new Token(TokenType.LPAREN, "("));
            case ')': return new Ok<>(new Token(TokenType.RPAREN, ")"));
            default:
                      StringBuilder sb = new StringBuilder("Unrecognized token ");
                      sb.append(start);
                      return new Err<>(sb.toString());
        }
    }

    private Result<Token, String> parseNumber() {
        while (this.current < this.length && (Character.isDigit(this.source.charAt(this.current)) || this.source.charAt(this.current) == '.')) {
            this.advance();
        }
        String substring = this.source.substring(this.start, this.current);
        try {
            Double value = Double.parseDouble(substring);
            return new Ok<>(new Token(TokenType.NUMBER, substring, value));
        } catch (NumberFormatException nfe) {
            return new Err<>(nfe.getLocalizedMessage());
        }
    }

    private Result<Token, String> parseIdentifier() {
        while (this.current < this.length
                && Character.isLetterOrDigit(
                    this.source.charAt(this.current))) {
            this.advance();
        }
        String substring = this.source.substring(this.start, this.current);
        if (this.constants.contains(substring)) {
            return new Ok<>(new Token(TokenType.CONSTANT, substring, substring));
        }
        if (this.functions.contains(substring)) {
            return new Ok<>(new Token(TokenType.FUNCTION, substring, substring));
        }
        return new Ok<>(new Token(TokenType.VARIABLE, substring, substring));
    }
}
