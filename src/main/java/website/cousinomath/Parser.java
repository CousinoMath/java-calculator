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

    public Result<ASTNode, String> parse() { return this.assignment(); }

    private Result<ASTNode, String> assignment() {
        Token current = this.peek(0);
        if (current.getType() == TokenType.VARIABLE) {
            Token next = this.peek(1);
            if (next.getType() == TokenType.EQUALS) {
                this.advance();
                this.advance();
                Result<ASTNode, String> exprResult = this.expression();
                if (exprResult.isErr()) {
                    return exprResult;
                }
                String name = current.getValue().map(Object::toString)
                    .orElse("");
                return new Ok<>(new ASTAssign(name, exprResult.unwrap()));
            }
        }
        return this.expression();
    }

    private Result<ASTNode, String> expression() {
        List<ASTNode> arguments = new ArrayList<ASTNode>();
        Result<ASTNode, String> result = this.factor();
        if (result.isErr()) {
            return result;
        }
        arguments.add(result.unwrap());
    loop:
        while (! this.atEOI()) {
            Token current = this.peek(0);
            switch(current.getType()) {
                case RPAREN:
                case EOI:
                    break loop;
                case PLUS:
                    this.advance();
                    result = this.factor();
                    if (result.isErr()) {
                        return result;
                    }
                    arguments.add(result.unwrap());
                    break;
                case DASH:
                    this.advance();
                    result = this.factor();
                    if (result.isErr()) {
                        return result;
                    }
                    ASTNode factor = result.unwrap();
                    ASTNumber mOne = new ASTNumber(-1.0);
                    arguments.add(new ASTTimes(Arrays.asList(mOne, factor)));
                    break;
                default:
                    this.advance();
                    StringBuilder sb = new StringBuilder("Expected plus or minus, not the token ");
                    sb.append(current.toString());
                    return new Err<>(sb.toString());

            }
        }
        switch (arguments.size()) {
            case 0: return new Ok<>(new ASTNumber(0.0));
            case 1: return new Ok<>(arguments.get(0));
            default: return new Ok<>(new ASTPlus(arguments));
        }
    }

    private Result<ASTNode, String> factor() {
        List<ASTNode> arguments = new ArrayList<ASTNode>();
        Result<ASTNode, String> result = this.exponential();
        if (result.isErr()) {
            return result;
        }
        arguments.add(result.unwrap());
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
                    result = this.exponential();
                    if (result.isErr()) {
                        return result;
                    }
                    arguments.add(result.unwrap());
                    break;
                case SLASH:
                    this.advance();
                    result = this.exponential();
                    if (result.isErr()) {
                        return result;
                    }
                    ASTNode exponential = result.unwrap();
                    ASTNumber mOne = new ASTNumber(-1.0);
                    arguments.add(new ASTPower(exponential, mOne));
                    break;
                default:
                    this.advance();
                    StringBuilder sb = new StringBuilder("Expected times or divides, not the token ");
                    sb.append(current.toString());
                    return new Err<>(sb.toString());
            }
        }
        switch (arguments.size()) {
            case 0: return new Ok<>(new ASTNumber(1.0));
            case 1: return new Ok<>(arguments.get(0));
            default: return new Ok<>(new ASTTimes(arguments));
        }
    }

    private Result<ASTNode, String> exponential() {
        Token current = this.peek(0);
        boolean negate = false;
        if (current.getType() == TokenType.DASH) {
            this.advance();
            negate = true;
        }
        Result<ASTNode, String> result = this.atom();
        if (result.isErr()) {
            return result;
        }
        ASTNode exponential = result.unwrap();
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
                result = this.exponential().map((ASTNode exp) -> new ASTPower(exponential, exp));
                break;
            default:
                this.advance();
                StringBuilder sb = new StringBuilder("Expected caret, not the token ");
                sb.append(current.toString());
                return new Err<>(sb.toString());
        }
        if (negate) {
            ASTNumber mOne = new ASTNumber(-1.0);
            result = result.map((ASTNode exp) -> new ASTTimes(Arrays.asList(mOne, exp)));
        }
        return result;
    }

    private Result<ASTNode, String> atom() {
        Token current = this.peek(0);
        Optional<Object> optional = current.getValue();
        Double value = Double.NaN;
        String name = "";
        Result<ASTNode, String> result;
        switch (current.getType()) {
            case NUMBER:
                this.advance();
                value = (Double)optional.get();
                return new Ok<>(new ASTNumber(value.doubleValue()));
            case VARIABLE:
                this.advance();
                name = (String)optional.get();
                return new Ok<>(new ASTVariable(name));
            case CONSTANT:
                this.advance();
                name = (String)optional.get();
                return new Ok<>(new ASTConstant(name));
            case FUNCTION:
                this.advance();
                name = (String)optional.get();
                result = this.atom();
                if (result.isErr()) {
                    return result;
                }
                ASTNode argument = result.unwrap();
                return new Ok<>(new ASTFunction(name, argument));
            case LPAREN:
                this.advance();
                result = this.expression();
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
