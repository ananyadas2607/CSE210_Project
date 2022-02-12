import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //String inputURL = args[0];
        String inputURL = "C:\\Users\\Daniel Paul\\Documents\\GitHub\\CSE210_Project\\Grammars\\Grammar_1.txt";
        //Read the grammar from the file
        Grammar grammar = new Grammar(inputURL);

        //State Diagram construction
        Diagram diagram= new Diagram(grammar);

        //Get the Reduce Set


        //Generate Table


        int a = 0;
    }





}
