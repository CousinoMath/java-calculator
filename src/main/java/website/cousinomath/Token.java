package website.cousinomath;

import java.util.Optional;

class Token {
    private TokenType type;
    private String lexeme;
    private Optional<Object> value;

    public Token(TokenType type, String lexeme, Object value) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = Optional.ofNullable(value);
    }

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = Optional.empty();
    }

    public TokenType getType() { return this.type; }
    public Optional<Object> getValue() { return this.value; }

    @Override
    public String toString() {
        return this.lexeme;
    }
}
