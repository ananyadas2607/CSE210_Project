from abc import ABC
from ParserTree import ParserTree


class Node(ParserTree, ABC):
    def __init__(self, symbol, children, start, end):
        self.symbol = symbol
        self.children = children
        self.start = start
        self.end = end

    def print1(self, prefix, continuation):
        print(prefix+self.symbol)
        for i in range(len(self.children)-1):
            self.children[i].print1(continuation + "├──", continuation + "│  ")
        self.children[-1].print1(continuation + "└──", continuation + "   ")
