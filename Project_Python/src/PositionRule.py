class PositionRule:
    def __init__(self, r, p):
        self.rule = r
        self.position = p

    def getNextSymbol(self):
        return self.rule.symbols[self.position]

    def isLast(self):
        return self.position == len(self.rule.symbols)
