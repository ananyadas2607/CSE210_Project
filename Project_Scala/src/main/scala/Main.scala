import scala.collection.convert.ImplicitConversions.`seq AsJavaList`
import scala.collection.mutable.Stack
import scala.collection.mutable.HashMap
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
    val parserTrees = parseSentences(table, grammar, sentences)

    //Output parsed sentences
    var counter = 1
    parserTrees.foreach(tree => {
      if (tree == null) {
        println("Sentence "+counter+": Not valid")
      } else {
        println("Sentence "+counter+": valid")
        tree.print("","")
      }
      counter+=1
    })

    //Time measurements
    var start = System.currentTimeMillis()
    for(i <- 1 to 1000){
      constructTable(grammar)
    }
    var end = System.currentTimeMillis()
    println("Time to generate table (x1000): "+((end-start)/1000.0))

    start = System.currentTimeMillis()
    for(i <- 1 to 1000){
      parseSentences(table, grammar, sentences)
    }
    end = System.currentTimeMillis()
    println("Time to generate table (x1000): "+((end-start)/1000.0))

  }

  def readSentences(inputUrl: String): List[String] = {
    val source = Source.fromFile(inputUrl)
    val lines = source.getLines()
    val sentences = new ListBuffer[String]

    while (lines.hasNext) {
      val line = lines.next()
      sentences.append(line+"$")
    }
    sentences.toList
  }

  def constructTable(grammar: Grammar): List[HashMap[String, Action]] = {
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

  def generateTable(diagram: Diagram, reducers: List[Reducer], grammar: Grammar): List[HashMap[String, Action]] = {
    val tableB = new ListBuffer[HashMap[String, Action]]
    for (i <- diagram.T.indices) {
      tableB.append(new HashMap[String, Action]())
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

  def printTable(table:List[HashMap[String,Action]], grammar:Grammar):Unit = {
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
      number + "  "
    } else {
      number + " "
    }
  }

  def parseSentences(
        table:List[HashMap[String,Action]],
        grammar:Grammar,
        sentences: List[String]):List[ParserTree] = {
    val list = new ListBuffer[ParserTree]
    sentences.foreach(sentence => {
      list.append(parseSentence(table, sentence, grammar))
    })
    list.toList
  }

  def parseSentence( table:List[HashMap[String,Action]], sentence:String, grammar:Grammar):ParserTree = {
    var state = 0
    var stack = new Stack[StackElement]
    stack.push(new StackElement("", 0, null))
    val input = sentence.split("")
    var counter = 0

    while(counter < input.length) {
      if (!table(state).contains(input(counter))){
        return null
      }
      val action = table(state)(input(counter))
      action.actionType match {
        case "shift" => {
          stack.push(new StackElement(input(counter), action.number, new Element(input(counter), counter, counter+1)))
          counter+=1
          state = action.number
        }
        case "reduce" => {
          val rule = grammar.rules.get(action.number)
          var children = new ListBuffer[ParserTree]
          val end = stack.head.tree.end
          for(i <- rule.symbols.indices) {
            val stackElement = stack.pop
            children.append(stackElement.tree)
          }
          var start = 0
          if (stack.head.tree != null) {
            start = stack.head.tree.end
          }
          children = children.reverse
          state = stack.head.state
          val action2 = table(state)(rule.result)
          val newNode = new Node(rule.result, children.toList, start, end)
          stack.push(new StackElement(rule.result, action2.number, newNode))
          state=action2.number
        }
        case "accept" => {
          return stack.pop.tree
        }
      }
    }
    null
  }
}

