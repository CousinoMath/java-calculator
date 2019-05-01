package website.cousinomath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

class App {
    public static void main(String[] args) throws IOException {
        boolean cont = true;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        TreeMap<String, Double> memory = new TreeMap<String, Double>();
        while (cont) {
            System.out.print("> ");
            String input = stdin.readLine();
            cont = input != null;
            if (cont) {
                Result<List<Token>, String> tokensResult = (new Scanner(input)).lex();
                if (tokensResult.isErr()) {
                    System.out.println(tokensResult.unwrapErr());
                    continue;
                }
                List<Token> tokens = tokensResult.unwrap();
                for (Token token : tokens) {
                    System.out.print(token.toString());
                }
                System.out.println("");
                Result<ASTNode, String> nodeResult = (new Parser(tokens)).parse();
                if (nodeResult.isErr()) {
                    System.out.println(nodeResult.unwrapErr());
                    continue;
                }
                ASTNode node = nodeResult.unwrap();
                System.out.println(node.toString());
                double value = node.evaluate(memory);
                System.out.println(value);
            }
            System.out.println("");
        }
    }
}
