import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String grammarName = args[0];
        String inputURL = "../../../../Grammars/"+grammarName+".txt";
        String sentenceURL="../../../../Grammars/Sentences/"+grammarName+".txt";
        //Read the grammar from the file
        Grammar grammar = new Grammar(inputURL);

        //Read sentences from file
        List<String> sentences = readSentences(sentenceURL);

        //Table construction
        List<HashMap<String, Action>> table = constructTable(grammar);

        //Output table
        printTable(table, grammar);

        //Parse Sentences
        List<ParserTree> parserTrees = parseSentences(table, grammar, sentences);

        //Output parsed sentences
        int counter = 1;
        for(ParserTree tree:parserTrees) {
            if (tree == null) {
                System.out.println("Sentence " + (counter) + ": Not valid");
            } else {
                System.out.println("Sentence " + (counter) + ": Valid");
                StringBuilder string = new StringBuilder();
                tree.print(string, "", "");
                System.out.println(string);
            }
            counter++;
        }

        //Time measurements
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            constructTable(grammar);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time to generate table (x1000): "+(end-start)/1000.0);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            parseSentences(table, grammar, sentences);
        }
        end = System.currentTimeMillis();
        System.out.println("Time to parse sentences (x1000): "+(end-start)/1000.0);

    }

    private static List<ParserTree> parseSentences(List<HashMap<String,Action>> table, Grammar grammar, List<String> sentences) {
        List<ParserTree> parserTrees = new ArrayList<>();
        for(String sentence:sentences){
            parserTrees.add(parseSentence(table, sentence, grammar));
        }
        return parserTrees;
    }

    private static List<HashMap<String,Action>> constructTable(Grammar grammar) {
        //State Diagram construction
        Diagram diagram= new Diagram(grammar);

        //Get the Reduce Set
        List<Reducer> reducers=generateReducers(diagram, grammar);

        //Generate Table
        return generateTable(diagram, reducers, grammar);
    }

    private static ParserTree parseSentence(List<HashMap<String, Action>> table, String sentence, Grammar grammar){
        int state = 0;
        Stack<StackElement> stack = new Stack<>();
        stack.push(new StackElement("", 0, null));
        String[] input = sentence.split("");
        int counter = 0;

        while(counter < input.length) {
            Action action = table.get(state).get(input[counter]);
            if (action == null) {
                return null;
            }
            switch (action.type) {
                case "shift":
                    stack.push(new StackElement(input[counter], action.number, new Element(input[counter], counter)));
                    counter++;
                    state = action.number;
                    break;
                case "reduce":
                    Rule rule = grammar.rules.get(action.number);
                    List<ParserTree> children = new ArrayList<>();
                    int end = stack.peek().tree.end;
                    for (int i = 0; i < rule.symbols.size(); i++) {
                        StackElement stackElement = stack.pop();
                        children.add(stackElement.tree);
                    }
                    int start = stack.peek().tree == null ? 0 : stack.peek().tree.end;
                    Collections.reverse(children);
                    state = stack.peek().state;
                    Action action2 = table.get(state).get(rule.result);
                    Node newNode = new Node(rule.result, children, start, end);
                    stack.push(new StackElement(rule.result, action2.number, newNode));
                    state = action2.number;
                    break;
                case "accept":
                    return stack.pop().tree;
            }
        }
        return null;
    }

    private static List<String> readSentences(String sentenceURL) throws FileNotFoundException {
        File inputFile = new File(sentenceURL);
        Scanner reader = new Scanner(inputFile);

        List<String> sentences=new ArrayList<>();

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            sentences.add(line + "$");
        }
        return sentences;
    }

    private static List<HashMap<String,Action>> generateTable(Diagram diagram, List<Reducer> reducers, Grammar grammar) {
        List<HashMap<String, Action>> table= new ArrayList<>();
        for(int i=0; i<diagram.T.size();i++){
            table.add(new HashMap<>());
        }
        for(Edge e: diagram.E){
            if(grammar.nonTerminals.contains(e.symbol)){
                table.get(e.start).put(e.symbol, new Action("goto",e.end));
            }
            else{
                table.get(e.start).put(e.symbol, new Action("shift",e.end));
            }
        }
        for(Reducer r:reducers){
            for(String terminal: grammar.terminals){
                table.get(r.state).put(terminal, new Action("reduce",r.ruleNum));
            }
        }
        for(State state:diagram.T){
            if(!state.rules.get(0).isLast() && state.rules.get(0).getNextSymbol().equals("$")){
                table.get(state.id).put("$",new Action("accept", 0));
            }
        }
        return table;
    }

    private static void printTable(List<HashMap<String,Action>> table, Grammar grammar) {
        StringBuilder header= new StringBuilder("    ");
        for(String terminal : grammar.terminals){
            header.append(terminal).append("   ");
        }

        for(String nonTerminal : grammar.nonTerminals){
            header.append(nonTerminal).append("   ");
        }

        System.out.println(header);


        for (int i = 0; i < table.size(); i++) {

            StringBuilder row = new StringBuilder();
            row.append(i);
            if(i<10){
                row.append("   ");
            }else{
                row.append("  ");
            }
            for(String terminal : grammar.terminals){
                Action action=table.get(i).get(terminal);
                if(action==null){
                    row.append("    ");
                }
                else{
                    switch (action.type){
                        case "shift":
                            row.append("s").append(printNumber(action.number));
                            break;
                        case "reduce":
                            row.append("r").append(printNumber(action.number));
                            break;
                        case "accept":
                            row.append("a" + "   ");
                            break;
                    }
                }
            }
            for(String nonTerminal : grammar.nonTerminals){
                Action action=table.get(i).get(nonTerminal);
                if(action==null){
                    row.append("    ");
                }
                else{
                    row.append("g").append(printNumber(action.number));
                }

            }
            System.out.println(row);

        }
    }
    private static String printNumber(int number){
        if (number < 10) {
            return number+"  ";
        } else {
            return number+" ";
        }
    }



    private static List<Reducer> generateReducers(Diagram diagram, Grammar grammar) {
        List<Reducer> reducers=new ArrayList<>();
        for(State state: diagram.T){
            for(PositionRule posRule: state.rules){
                if(posRule.isLast())
                {
                    reducers.add(new Reducer(state.id, posRule.rule.number));
                }
            }
        }
        return reducers;
    }




}
