class Node(var symbol:String, var children:List[ParserTree], override val start:Int, override val end:Int) extends ParserTree(start,end) {
  def print(prefix:String, continuation:String):Unit = {
    println(prefix + symbol)
    for(i <- 0 until children.size-1) {
      children(i).print(continuation+ "├──", continuation + "│  ")
    }
    children.last.print(continuation + "└──", continuation+ "   ")
  }
}
