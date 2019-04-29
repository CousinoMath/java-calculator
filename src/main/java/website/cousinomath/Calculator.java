package website.cousinomath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

class Calculator {
    public static void main(String[] args) throws IOException {
        boolean cont = true;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        TreeMap<String, Double> memory = new TreeMap<String, Double>();
        while (cont) {
            System.out.print("> ");
            String input = stdin.readLine();
            cont = input != null;
            if (cont) {
                List<Token> tokens = (new Scanner(input)).lex();
                for (Token token : tokens) {
                    System.out.print(token.toString());
                }
                System.out.println("");
                ASTNode node = (new Parser(tokens)).parse();
                System.out.println(node.toString());
                double value = node.evaluate(memory);
                System.out.println(value);
            }
            System.out.println("");
        }
    }
}
