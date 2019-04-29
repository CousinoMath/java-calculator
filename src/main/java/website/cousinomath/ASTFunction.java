package website.cousinomath;

import java.util.function.Function;
import java.util.Map;

class ASTFunction extends ASTNode {
    private final String name;
    private final Function<Double, Double> function;
    private final ASTNode argument;

    public ASTFunction(String name, ASTNode argument) {
        this.name = name;
        this.argument = argument;
        switch (name) {
            case "acos":
                this.function = (x) -> Math.acos(x);
                break;
            case "asin":
                this.function = Math::asin;
                break;
            case "atan":
                this.function = Math::atan;
                break;
            case "cos":
                this.function = Math::cos;
                break;
            case "exp":
                this.function = Math::exp;
                break;
            case "log":
                this.function = Math::log;
                break;
            case "sin":
                this.function = Math::sin;
                break;
            case "tan":
                this.function = Math::tan;
                break;
            default:
                this.function = (x) -> Double.NaN;
                break;
        }
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        double argEvaled = this.argument.evaluate(memory);
        return this.function.apply(argEvaled).doubleValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(this.name);
        sb.append(" ");
        sb.append(this.argument.toString());
        sb.append(")");
        return sb.toString();
    }
}
