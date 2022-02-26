class State(var id:Int, var rules:List[PositionRule]) {
  def closure(grammar:Grammar): Unit = {
    var i = 0
    while (i < rules.size) {
      if(!rules(i).isLast) {
        val symbol = rules(i).getNextSymbol
        if(grammar.nonTerminals.contains(symbol)){
          grammar.rules.foreach(r => {
            if(r.result.equals(symbol)) {
              val pr = new PositionRule(r, 0)
              if(!containsRule(pr)) {
                rules = rules :+ pr
              }
            }
          })
        }
      }
      i = i+1
    }
  }

  def containsRule(pr: PositionRule):Boolean = {
    rules.foreach(rule => {
      if (rule.position.equals(pr.position) && rule.rule.equals(pr.rule)) {
        return true
      }
    })
    false
  }
}
