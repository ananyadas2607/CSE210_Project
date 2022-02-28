class Grammar:
    nonTerminals = []
    terminals = []

    def __init__(self, inputUrl, rules):
        f = open(inputUrl, 'r')

        self.nonTerminals.append("R")
        self.nonTerminals.appendAll(f.readline().split(" "))

        self.terminals.appendAll(f.readline().split(" "))
        self.terminals.append("$")

        line = f.readline()
        while line:
            rule = (line.split("→")[0], line.split("→")[1].split(""), rules.len())
            rules.add(rule)
            line = f.readline()
        f.close()
