class Element(var symbol:String, override val start:Int, override val end:Int) extends ParserTree(start,end) {
  def print(prefix:String, continuation:String):Unit = {
    println(prefix + symbol)
  }
}
