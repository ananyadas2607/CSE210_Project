public abstract class ParserTree {
    abstract void print(StringBuilder string, String prefix, String continuation);
    int start;
    int end;
}