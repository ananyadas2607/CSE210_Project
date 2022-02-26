public class StackElement {
    String symbol;
    int state;
    ParserTree tree;

    StackElement(String symbol, int state, ParserTree tree){
        this.symbol = symbol;
        this.state = state;
        this.tree = tree;
    }
}
