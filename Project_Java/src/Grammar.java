import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Grammar {

    List<String> terminals;
    List<String> nonTerminals;
    List<Rule> rules;

    Grammar(String inputURL) throws FileNotFoundException {
        File inputFile = new File(inputURL);
        Scanner reader = new Scanner(inputFile);

        this.nonTerminals = new ArrayList<>();
        this.nonTerminals.add("R");
        this.nonTerminals.addAll(Arrays.asList(reader.nextLine().split(" ")));

        this.terminals = new ArrayList<>();
        this.terminals.addAll(Arrays.asList(reader.nextLine().split(" ")));
        this.terminals.add("$");

        this.rules = new ArrayList<>();
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Rule rule = new Rule(line.split("→")[0], Arrays.asList(line.split("→")[1].split("")));
            this.rules.add(rule);
        }
        reader.close();
    }
}
