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

    void print(StringBuilder string, String prefix, String continuation) {
        string.append(prefix).append(symbol).append("\n");
        for (int i = 0; i < children.size()-1; i++){
            children.get(i).print(string, continuation+"├──", continuation+"│  ");
        }
        children.get(children.size()-1).print(string, continuation+"└──", continuation+"   ");
    }
}
