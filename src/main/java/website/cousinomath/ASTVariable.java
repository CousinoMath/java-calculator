package website.cousinomath;

import java.util.Map;

class ASTVariable extends ASTNode {
    private final String name;

    public ASTVariable(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        Double value = memory.get(this.name);
        if (value == null) {
            value = Double.NaN;
        }
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
