import java.util.ArrayList;
import java.util.List;

public class Node extends ParserTree{
    String symbol;
    public List<ParserTree> children;

    Node(String symbol, List<ParserTree> children, int start, int end) {
        this.symbol = symbol;
        this.children = children;
        this.start = start;
        this.end = end;
    }
}
