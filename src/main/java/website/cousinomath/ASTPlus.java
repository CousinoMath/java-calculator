package website.cousinomath;

import java.util.List;
import java.util.Map;

class ASTPlus extends ASTNode {
    private final List<ASTNode> arguments;

    public ASTPlus(List<ASTNode> arguments) {
        this.arguments = arguments;
    }

    @Override
    public double evaluate(Map<String, Double> memory) {
        return this.arguments.stream().map(arg -> arg.evaluate(memory))
            .reduce(0.0, (first, second) -> first + second);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(+");
        for (ASTNode argument : this.arguments) {
            sb.append(" ");
            sb.append(argument.toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
