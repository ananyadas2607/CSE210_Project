import State
import Edge


def equalStates(s, J):
    i=0
    if not s.rules.len() == len(J.rules):
        return False
    while i < s.rules.len():
        pr1 = s.rules[i]
        pr2 = J.rules[i]
        if not pr1.position == pr2.position or not pr1.rule == pr2.rule:
            return False
    return True


def Goto(I, X, grammar):
    J = self.T.len()
    for rule in I.rules:
        if rule.isLast() and rule.getNextSymbol() == X:
            J.rules.add(rule.rule, rule.position + 1)
    J.closure(grammar)
    return J


class Diagram:
    def __init__(self, grammar):
        #Initialize the graph
        self.T = []
        self.E = []

        #Add the first state
        root = State[0]
        root.rules.append(positionRule(grammar.rules[0], 0))
        root.closure(grammar)
        self.T.append(root)

        i = 0
        while i < len(self.T):
            I = self.T[i]
            for rule in I.rules:
                if not rule.isLast():
                    X = rule.getNextSymbol()
                    if not X.equals("$"):
                        J = Goto(I, X, grammar)
                        # Check if there is already a state with the same rules
                        repeated = False
                        for s in self.T:
                            if equalStates(s, J):
                                edgePresent = False
                                for e in self.E:
                                    if e.start == I.id and e.end == s.id:
                                        edgePresent = True

                                if not edgePresent:
                                    self.E.append(Edge[X, I.id, s.id])

                                repeated = True
                                break

                        if not repeated:
                            self.T.append(J)
                            self.E.append(Edge[X, I.id, J.id])
        i += 1
