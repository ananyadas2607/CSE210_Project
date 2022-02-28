from ParserTree import ParserTree


class Element(ParserTree):
    def __init__(self,symbol,start):
        self.symbol = symbol
        self.start = start
        self.end = self.start + 1
        self.string = string

    def print1(self, string, prefix, continuation):
        string = string + prefix + self.symbol + '\n'









