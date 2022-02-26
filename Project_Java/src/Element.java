public class Element extends ParserTree {
    String symbol;

    Element(String symbol, int start) {
        this.symbol = symbol;
        this.start = start;
        this.end = start + 1;
    }

    void print(StringBuilder string, String prefix, String continuation) {
        string.append(prefix).append(symbol).append("\n");
    }
}
