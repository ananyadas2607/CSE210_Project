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
    printTable(table, grammar)

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

  def constructTable(grammar: Grammar): List[mutable.HashMap[String, Action]] = {
    //State Diagram construction
    val diagram= new Diagram(null, null);
    diagram.generate(grammar);

    //Get the Reduce Set
    val reducers=generateReducers(diagram, grammar);

    //Generate Table
    generateTable(diagram, reducers, grammar);
  }

  def generateReducers(diagram: Diagram, grammar: Grammar):List[Reducer] = {
    val reducers = new ListBuffer[Reducer]
    diagram.T.foreach(state => {
      state.rules.foreach( posRule => {
        if(posRule.isLast){
          reducers.append(new Reducer(state.id, posRule.rule.number))
        }
      })
    })
    reducers.toList
  }

  def generateTable(diagram: Diagram, reducers: List[Reducer], grammar: Grammar): List[mutable.HashMap[String, Action]] = {
    val tableB = new ListBuffer[mutable.HashMap[String, Action]]
    for (i <- diagram.T.indices) {
      tableB.append(new mutable.HashMap[String, Action]())
    }
    val table = tableB.toList
    diagram.E.foreach(e => {
      if(grammar.nonTerminals.contains(e.symbol)) {
        table(e.start).put(e.symbol, new Action("goto", e.end))
      } else {
        table(e.start).put(e.symbol, new Action("shift", e.end))
      }
    })
    reducers.foreach(r => {
      grammar.terminals.foreach(terminal => {
        table(r.state).put(terminal, new Action("reduce", r.ruleNum))
      })
    })
    diagram.T.foreach(state => {
      if(!state.rules.head.isLast && state.rules.head.getNextSymbol.equals("$")) {
        table(state.id).put("$", new Action("accept", 0))
      }
    })
    table
  }

  def printTable(table:List[mutable.HashMap[String,Action]], grammar:Grammar):Unit = {
    var header = "    "
    grammar.terminals.foreach(terminal => {
      header += terminal + "   "
    })
    grammar.nonTerminals.foreach(nonTerminal => {
      header += nonTerminal + "   "
    })
    println(header)

    for( i <- table.indices) {
      var row  = ""+i
      if(i<10) {
        row += "   "
      } else {
        row += "  "
      }
      grammar.terminals.foreach(terminal => {
        if (!table(i).contains(terminal)) {
          row+="    "
        } else {
          val action = table(i)(terminal)
          action.actionType match {
            case "shift" => row+="s"+printNumber(action.number)
            case "reduce" => row+="r"+printNumber(action.number)
            case "accept" => row+="a   "
          }
        }
      })
      grammar.nonTerminals.foreach(nonTerminal => {
        if (!table(i).contains(nonTerminal)) {
          row+="    "
        } else {
          val action = table(i)(nonTerminal)
          row+="g"+printNumber(action.number)
        }
      })
      System.out.println(row)
    }
  }

  def printNumber(number:Int):String = {
    if (number < 10) {
      return number + "  "
    } else {
      return number + " "
    }
  }
}

