class Node(ParserTree):
    def __init__(self, symbol, children, start, end):
        self.symbol=symbol
        self.children=children
        self.start=start
        self.end=end

    def print(self, string, prefix, continuation):
        string.append(prefix).append(symbol).append("\n")
        i=0
        while i <  self.children.len()-1 :
            self.children[i].print(string, continuation+"├──", continuation+"│  ")
        children[(children.len() - 1).print(string, continuation + "└──", continuation + "   ")]