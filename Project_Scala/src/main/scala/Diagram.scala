import scala.collection.mutable.ListBuffer

class Diagram(var T:List[State], var E:List[Edge]) {
  def generate(grammar:Grammar): Unit = {
    val Tb = new ListBuffer[State]
    val Eb = new ListBuffer[Edge]

    val root = new State(0, List())
    root.rules = root.rules :+ new PositionRule(grammar.rules.head,0)
    root.closure(grammar)

    Tb.append(root)

    var i = 0
    while (i < Tb.size) {
      val I = Tb(i)
      I.rules.foreach(rule =>{
        if(!rule.isLast) {
          val X = rule.getNextSymbol
          if(!X.equals("$")) {
            val J = Goto(I, X, grammar, Tb)
            var repeated = false
            Tb.foreach(s => {
              if (equalStates(s,J)) {
                var edgePresent = false
                Eb.foreach(e => {
                  if(e.start==I.id && e.end == s.id) {
                    edgePresent = true
                  }
                })
                if(!edgePresent) {
                  Eb.append(new Edge(X, I.id, s.id))
                }
                repeated = true
              }
            })
            if (!repeated) {
              Tb.append(J)
              Eb.append(new Edge(X, I.id, J.id))
            }
          }
        }
      })
      i = i+1
    }
    T = Tb.toList
    E = Eb.toList
  }

  def Goto(I:State, X:String, grammar: Grammar, Tb:ListBuffer[State]):State = {
    val J = new State(Tb.size, List())
    I.rules.foreach(rule => {
      if(!rule.isLast && rule.getNextSymbol.equals(X)) {
        J.rules = J.rules :+ new PositionRule(rule.rule, rule.position+1)
      }
    })
    J.closure(grammar)
    J
  }

  def equalStates(s:State, J:State):Boolean = {
    if (s.rules.size != J.rules.size) {
      return false
    }
    for(i <- s.rules.indices) {
      val pr1 = s.rules(i)
      val pr2 = J.rules(i)
      if(!pr1.position.equals(pr2.position) || !pr1.rule.equals(pr2.rule)) {
        return false
      }
    }
    true
  }
}
