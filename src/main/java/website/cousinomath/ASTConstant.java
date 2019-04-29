package website.cousinomath;

import java.util.Map;

class ASTConstant extends ASTNode {
    private final String name;
    private final double value;

    public ASTConstant(String name) {
        this.name = name;
        switch (name) {
            case "pi":
                this.value = Math.PI;
                break;
            case "e":
                this.value = Math.E;
                break;
            default:
                this.value = Double.NaN;
                break;
        }
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        return this.value;
    }

    @Override public String toString() {
        return this.name;
    }
}
