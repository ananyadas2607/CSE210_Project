public class PositionRule {
    Rule rule;
    int position;

    PositionRule(Rule r, int p){
        rule=r;
        position=p;
    }

    String getNextSymbol() {
        return rule.symbols.get(position);
    }

    boolean isLast() {
        return position == rule.symbols.size();
    }
}
