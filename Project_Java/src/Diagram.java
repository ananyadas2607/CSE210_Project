import java.util.ArrayList;
import java.util.List;

public class Diagram {
    List<State> T;
    List<Edge> E;

    Diagram(Grammar grammar){
        //Initialize the graph
        T = new ArrayList<>();
        E = new ArrayList<>();

        //Add the first state
        State root = new State(0);
        root.rules.add(new PositionRule(grammar.rules.get(0),0));
        root.closure(grammar);
        T.add(root);

        int i=0;
        while (i < T.size()) {
            State I = T.get(i);
            for(PositionRule rule:I.rules){
                if(!rule.isLast()) {
                    String X = rule.getNextSymbol();
                    if(!X.equals("$")) {
                        State J = Goto(I, X, grammar);
                        //Check if there is already a state with the same rules
                        boolean repeated = false;
                        for(State s:T){
                            if (equalStates(s,J)) {
                                boolean edgePresent= false;
                                for(Edge e:E){
                                    if (e.start == I.id && e.end == s.id) {
                                        edgePresent = true;
                                        break;
                                    }
                                }
                                if(!edgePresent) {
                                    E.add(new Edge(X, I.id, s.id));
                                }
                                repeated = true;
                                break;
                            }
                        }
                        if(!repeated) {
                            T.add(J);
                            E.add(new Edge(X, I.id, J.id));
                        }
                    }
                }
            }
            i++;
        }
    }

    private boolean equalStates(State s, State J) {
        if (s.rules.size() != J.rules.size()){
            return false;
        }
        for(int i=0; i< s.rules.size(); i++) {
            PositionRule pr1 = s.rules.get(i);
            PositionRule pr2 = J.rules.get(i);
            if (pr1.position != pr2.position || !pr1.rule.equals(pr2.rule) ) {
                return false;
            }
        }
        return true;
    }

    public State Goto(State I, String X, Grammar grammar) {
        State J = new State(T.size());
        for (PositionRule rule:I.rules) {
            if(!rule.isLast() && rule.getNextSymbol().equals(X)){
                J.rules.add(new PositionRule(rule.rule, rule.position+1));
            }
        }
        J.closure(grammar);
        return J;
    }
}
