from Rule import Rule


class Grammar:
    nonTerminals = []
    terminals = []
    rules = []

    def __init__(self, inputUrl):
        f = open(inputUrl, 'r')

        self.nonTerminals += (f.readline().strip("\n").split(" "))

        self.terminals += (f.readline().strip("\n").split(" "))
        self.terminals.append("$")

        line = f.readline()
        while line:
            rule = Rule(line.split("->")[0], list(line.split("->")[1].strip("\n")), len(self.rules))
            self.rules.append(rule)
            line = f.readline()
        f.close()
