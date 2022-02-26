import scala.collection.mutable.ListBuffer
import scala.io.Source

class Grammar(var terminals : List[String], var nonTerminals : List[String], var rules : List[Rule]) {
  def readGrammar(inputUrl : String): Unit = {
    val source = Source.fromFile(inputUrl)
    val lines = source.getLines()
    nonTerminals = lines.next().split(" ").toList
    terminals = lines.next().split(" ").toList
    terminals = terminals :+ "$"

    val rulesB = new ListBuffer[Rule]()
    while (lines.hasNext) {
      val line = lines.next()
      rulesB += new Rule(line.split("->")(0), line.split("->")(1).split("").toList, rulesB.length)
    }
    rules = rulesB.toList
    source.close()
  }
}
