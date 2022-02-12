import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //String inputURL = args[0];
        String inputURL = "/Users/ananya/Documents/GitHub/CSE210_Project/Grammars/Grammar_1.txt";
        //Read the grammar from the file
        Grammar grammar = new Grammar(inputURL);

        //State Diagram construction
        Diagram diagram= new Diagram(grammar);

        //Get the Reduce Set
        List<Reducer> reducers=generateReducers(diagram, grammar);

        //Generate Table
        List<HashMap<String, Action>> table=generateTable(diagram, reducers, grammar);


    int a=0;

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
