from ParserTree import ParserTree


class Element(ParserTree):
    def __init__(self, symbol, start):
        self.symbol = symbol
        self.start = start
        self.end = self.start + 1

    def print1(self, prefix, continuation):
        print(prefix + self.symbol)
