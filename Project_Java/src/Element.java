public class Element extends ParserTree {
    String symbol;

    Element(String symbol, int start) {
        this.symbol = symbol;
        this.start = start;
        this.end = start + 1;
    }
}
