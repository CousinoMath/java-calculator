package website.cousinomath;

import java.util.Map;

class ASTAssign extends ASTNode {
    private final String name;
    private final ASTNode expr;

    public ASTAssign(String name, ASTNode expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        final double expr = this.expr.evaluate(memory);
        memory.put(this.name, expr);
        return expr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(= ");
        sb.append(this.name);
        sb.append(" ");
        sb.append(expr.toString());
        sb.append(")");
        return sb.toString();
    }
}
