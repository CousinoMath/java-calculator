package website.cousinomath;

import java.util.Map;

class ASTPower extends ASTNode {
    private final ASTNode base;
    private final ASTNode exp;

    public ASTPower(ASTNode base, ASTNode exp) {
        this.base = base;
        this.exp = exp;
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        final double base = this.base.evaluate(memory);
        final double exp = this.exp.evaluate(memory);
        return Math.pow(base, exp);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(^ ");
        sb.append(this.base.toString());
        sb.append(" ");
        sb.append(this.exp.toString());
        sb.append(")");
        return sb.toString();
    }
}
