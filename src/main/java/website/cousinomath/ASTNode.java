package website.cousinomath;

import java.util.Map;

abstract class ASTNode {
    abstract public double evaluate(Map<String, Double> memory);
}
