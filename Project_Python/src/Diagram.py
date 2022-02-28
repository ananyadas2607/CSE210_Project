from Edge import Edge
from PositionRule import PositionRule
from State import State


def equalStates(s, J):
    if not len(s.rules) == len(J.rules):
        return False
    for i in range(len(s.rules)):
        pr1 = s.rules[i]
        pr2 = J.rules[i]
        if (not pr1.position == pr2.position) or (not pr1.rule == pr2.rule):
            return False
    return True


class Diagram:
    def __init__(self, grammar):
        # Initialize the graph
        self.T = []
        self.E = []

        # Add the first state
        root = State(0)
        root.rules.append(PositionRule(grammar.rules[0], 0))
        root.closure(grammar)
        self.T.append(root)

        i = 0
        while i < len(self.T):
            IState = self.T[i]
            for rule in IState.rules:
                if not rule.isLast():
                    X = rule.getNextSymbol()
                    if not X == "$":
                        J = self.Goto(IState, X, grammar)
                        # Check if there is already a state with the same rules
                        repeated = False
                        for s in self.T:
                            if equalStates(s, J):
                                edgePresent = False
                                for e in self.E:
                                    if e.start == IState.id and e.end == s.id:
                                        edgePresent = True

                                if not edgePresent:
                                    self.E.append(Edge(X, IState.id, s.id))

                                repeated = True
                                break

                        if not repeated:
                            self.T.append(J)
                            self.E.append(Edge(X, IState.id, J.id))
            i += 1

    def Goto(self, IState, X, grammar):
        J = State(len(self.T))
        for rule in IState.rules:
            if (not rule.isLast()) and rule.getNextSymbol() == X:
                J.rules.append(PositionRule(rule.rule, rule.position + 1))
        J.closure(grammar)
        return J
