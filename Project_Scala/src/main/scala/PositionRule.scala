class PositionRule(var rule:Rule, var position:Int) {
  def getNextSymbol:String = {
    rule.symbols(position)
  }
  def isLast:Boolean = {
    position == rule.symbols.size
  }
}
