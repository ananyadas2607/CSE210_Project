import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {

  def main(args: Array[String]) = {
    //val grammarName: String = args(0)
    //val inputURL: String = "../../../../Grammars/" + grammarName + ".txt"
    //val sentenceURL: String = "../../../../Grammars/Sentences/" + grammarName + ".txt"

    //Read the grammar from the file
    val grammar: Grammar = new Grammar(null, null, null)
    grammar.readGrammar("C:\\Users\\Daniel Paul\\Documents\\GitHub\\CSE210_Project\\Grammars\\Grammar_1.txt")

    //Read the sentences from the file
    val sentences = readSentences("C:\\Users\\Daniel Paul\\Documents\\GitHub\\CSE210_Project\\Grammars\\Sentences\\Grammar_1.txt")

    //Table construction
    val table = constructTable(grammar)

    //Output table
    //printTable(table, grammar)

    //Parse Sentences
    //val parserTrees = parseSentences(table, grammar, sentences)

    //Output parsed sentences
    var counter = 1
    //parserTrees.foreach(tree => {

    //})

    //Time measurements

  }

  def readSentences(inputUrl: String): List[String] = {
    val source = Source.fromFile(inputUrl)
    val lines = source.getLines()
    val sentences = new ListBuffer[String]

    while (lines.hasNext) {
      val line = lines.next()
      sentences +: (line+"$")
    }
    return sentences.toList
  }

  //def constructTable(grammar: Grammar): List[mutable.HashMap[String, Action]] = {
  def constructTable(grammar: Grammar): Unit = {
    //State Diagram construction
    val diagram= new Diagram(null, null);
    diagram.generate(grammar);

    //Get the Reduce Set
    //val reducers=generateReducers(diagram, grammar);

    //Generate Table
    //return generateTable(diagram, reducers, grammar);
  }
}
