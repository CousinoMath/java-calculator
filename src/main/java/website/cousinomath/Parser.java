package website.cousinomath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Parser {
    private final List<Token> tokens;
    private final int length;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.length = tokens.size();
    }

    private Token peek(int lookahead) {
        if (this.current + lookahead < this.length) {
            return this.tokens.get(this.current + lookahead);
        }
        return this.tokens.get(this.length - 1);
    }

    private void advance() {
        if (this.current < this.length) {
            this.current++;
        }
    }

    private boolean atEOI() {
        return this.current >= this.length
            || this.tokens.get(this.current).getType() == TokenType.EOI;
    }

    public ASTNode parse() { return this.assignment(); }

    private ASTNode assignment() {
        Token current = this.peek(0);
        if (current.getType() == TokenType.VARIABLE) {
            Token next = this.peek(1);
            if (next.getType() == TokenType.EQUALS) {
                this.advance();
                this.advance();
                ASTNode expr = this.expression();
                String name = current.getValue().map(Object::toString)
                    .orElse("");
                return new ASTAssign(name, expr);
            }
        }
        return this.expression();
    }

    private ASTNode expression() {
        List<ASTNode> arguments = new ArrayList<ASTNode>();
        arguments.add(this.factor());
    loop:
        while (! this.atEOI()) {
            Token current = this.peek(0);
            switch(current.getType()) {
                case RPAREN:
                case EOI:
                    break loop;
                case PLUS:
                    this.advance();
                    arguments.add(this.factor());
                    break;
                case DASH:
                    this.advance();
                    ASTNode factor = this.factor();
                    ASTNumber mOne = new ASTNumber(-1.0);
                    arguments.add(new ASTTimes(Arrays.asList(mOne, factor)));
                    break;
                default:
                    this.advance();
                    StringBuilder sb = new StringBuilder("Expected plus or minus, not the token ");
                    sb.append(current.toString());
                    throw new ArithmeticException(sb.toString());

            }
        }
        switch (arguments.size()) {
            case 0: return new ASTNumber(0.0);
            case 1: return arguments.get(0);
            default: return new ASTPlus(arguments);
        }
    }

    private ASTNode factor() {
        List<ASTNode> arguments = new ArrayList<ASTNode>();
        arguments.add(this.exponential());
    loop:
        while (! this.atEOI()) {
            Token current = this.peek(0);
            switch (current.getType()) {
                case PLUS:
                case DASH:
                case EOI:
                case RPAREN:
                    break loop;
                case STAR:
                    this.advance();
                    arguments.add(this.exponential());
                    break;
                case SLASH:
                    this.advance();
                    ASTNode exponential = this.exponential();
                    ASTNumber mOne = new ASTNumber(-1.0);
                    arguments.add(new ASTPower(exponential, mOne));
                    break;
                default:
                    this.advance();
                    StringBuilder sb = new StringBuilder("Expected times or divides, not the token ");
                    sb.append(current.toString());
                    throw new ArithmeticException(sb.toString());
            }
        }
        switch (arguments.size()) {
            case 0: return new ASTNumber(1.0);
            case 1: return arguments.get(0);
            default: return new ASTTimes(arguments);
        }
    }

    private ASTNode exponential() {
        Token current = this.peek(0);
        boolean negate = false;
        if (current.getType() == TokenType.DASH) {
            this.advance();
            negate = true;
        }
        ASTNode result = this.atom();
        current = this.peek(0);
        switch (current.getType()) {
            case PLUS:
            case DASH:
            case STAR:
            case SLASH:
            case EOI:
            case RPAREN:
                break;
            case CARET:
                this.advance();
                result = new ASTPower(result, this.exponential());
                break;
            default:
                this.advance();
                StringBuilder sb = new StringBuilder("Expected caret, not the token ");
                sb.append(current.toString());
                throw new ArithmeticException(sb.toString());
        }
        if (negate) {
            ASTNumber mOne = new ASTNumber(-1.0);
            result = new ASTTimes(Arrays.asList(mOne, result));
        }
        return result;
    }

    private ASTNode atom() {
        Token current = this.peek(0);
        Optional<Object> optional = current.getValue();
        Double value = Double.NaN;
        String name = "";
        switch (current.getType()) {
            case NUMBER:
                this.advance();
                value = (Double)optional.get();
                return new ASTNumber(value.doubleValue());
            case VARIABLE:
                this.advance();
                name = (String)optional.get();
                return new ASTVariable(name);
            case CONSTANT:
                this.advance();
                name = (String)optional.get();
                return new ASTConstant(name);
            case FUNCTION:
                this.advance();
                name = (String)optional.get();
                ASTNode argument = this.atom();
                return new ASTFunction(name, argument);
            case LPAREN:
                this.advance();
                ASTNode result = this.expression();
                this.consume(")");
                return result;
            default:
                this.advance();
                StringBuilder sb = new StringBuilder("Expected an atom, not the token ");
                sb.append(current.toString());
                throw new ArithmeticException(sb.toString());
        }
    }

    private void consume(String lexeme) {
        Token current = this.peek(0);
        if (current.toString() == lexeme) {
            this.advance();
            return;
        }
        StringBuilder sb = new StringBuilder("Expected to see '");
        sb.append(lexeme);
        sb.append("'");
        throw new ArithmeticException(sb.toString());
    }
}
