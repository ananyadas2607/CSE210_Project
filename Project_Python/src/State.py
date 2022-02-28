from PositionRule import PositionRule


class State:
    def __init__(self, id):
        self.id = id
        self.rules = []

    def closure(self, grammar):
        i = 0
        while i < len(self.rules):
            # We check if the rule is pointing somewhere
            if not self.rules[i].isLast():
                # We obtain the symbol and check if it is a non-terminal
                symbol = self.rules[i].getNextSymbol()
                if symbol in grammar.nonTerminals:
                    # We iterate through the rules of the grammar
                    for r in grammar.rules:
                        # If the rule starts with the symbol we are looking at
                        if r.result == symbol:
                            # We try to add the rule to the state
                            pr = PositionRule(r, 0)
                            if not self.containsRule(pr):
                                self.rules.append(pr)
            i += 1

    def containsRule(self, pr):
        for rule in self.rules:
            if rule.position == pr.position and rule.rule == pr.rule:
                return True
        return False
