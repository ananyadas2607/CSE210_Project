import java.util.ArrayList;
import java.util.List;

public class State {
    int id;
    List<PositionRule> rules;

    State(int id) {
        this.id=id;
        rules = new ArrayList<>();
    }

    public void closure(Grammar grammar) {
        int i = 0;
        while(i<rules.size()){
            //We check if the rule is pointing somewhere
            if (!rules.get(i).isLast()) {
                //We obtain the symbol and check if it is a non-terminal
                String symbol = rules.get(i).getNextSymbol();
                if (grammar.nonTerminals.contains(symbol)) {
                    //We iterate through the rules of the grammar
                    for(Rule r:grammar.rules) {
                        //If the rule starts with the symbol we are looking at
                        if (r.result.equals(symbol)){
                            //We try to add the rule to the state
                            PositionRule pr = new PositionRule(r,0);
                            if(!containsRule(pr)) {
                                rules.add(pr);
                            }
                        }
                    }
                }
            }
            i++;
        }
    }

    private boolean containsRule(PositionRule pr) {
        for(PositionRule rule:rules) {
            if (rule.position == pr.position && rule.rule.equals(pr.rule) ) {
                return true;
            }
        }
        return false;
    }
}
