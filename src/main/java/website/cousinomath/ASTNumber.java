package website.cousinomath;

import java.util.Map;

class ASTNumber extends ASTNode {
    private final double value;

    public ASTNumber(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        return this.value;
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }
}
