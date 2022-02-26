import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        //String inputURL = args[0];
        String inputURL = "/Users/ananya/Documents/GitHub/CSE210_Project/Grammars/Grammar_1.txt";

        String sentenceURL="/Users/ananya/Documents/GitHub/CSE210_Project/Grammars/Sentences/Grammar_1.txt";

        //Read the grammar from the file
        Grammar grammar = new Grammar(inputURL);

        long start = System.currentTimeMillis();
        //State Diagram construction
        Diagram diagram= new Diagram(grammar);

        //Get the Reduce Set
        List<Reducer> reducers=generateReducers(diagram, grammar);

        //Generate Table
        List<HashMap<String, Action>> table=generateTable(diagram, reducers, grammar);
        long end = System.currentTimeMillis();

        //Output table and time elapsed
        //printTable(table);

        System.out.println("Time elapsed: " + (end-start)/1000.0);

        //Read sentences from file
        List<String> sentences= readSentences(sentenceURL);

        //Parse sentences
        start = System.currentTimeMillis();
        for(String sentence:sentences){
            parseSentence(table, sentence, grammar);
        }
        end = System.currentTimeMillis();

        //Output parsed sentences and time elapsed
        System.out.println("Time elapsed: " + (end-start)/1000.0);






    int a=0;


    }

    private static void parseSentence(List<HashMap<String, Action>> table, String sentence, Grammar grammar){
        int state = 0;
        Stack<StackElement> stack = new Stack<>();
        stack.push(new StackElement("", 0));
        String[] input = sentence.split("");
        int counter = 0;

        while(counter < input.length) {
            Action action = table.get(state).get(input[counter]);
            if (action.type.equals("shift")) {
                stack.push(new StackElement(input[counter], action.number));
                counter++;
                state=action.number;
            }
            else if(action.type.equals("reduce")){
                Rule rule=grammar.rules.get(action.number);
                for(int i=0;i<rule.symbols.size();i++){
                    stack.pop();
                }
                state=stack.peek().state;
                Action action2=table.get(state).get(rule.result);
                stack.push(new StackElement(rule.result, action2.number));
                state=action2.number;
            }
            else if(action.type.equals("accept")){
                    break;
            }
        }

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
