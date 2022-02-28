import sys
import time

from Action import Action
from Diagram import Diagram
from Element import Element
from Grammar import Grammar
from Node import Node
from Reducer import Reducer
from Stackelement import Stackelement


def generateReducers(diagram):
    reducers = []
    for state in diagram.T:
        for positionRule in state.rules:
            if positionRule.isLast():
                reducers.append(Reducer(state.id, positionRule.rule.number))

    return reducers


def readSentences(sentenceURL):
    f = open(sentenceURL, "r")
    sentences = []

    for line in f:
        sentences.append(line.strip("\n") + "$")
    return sentences


def generateTable(diagram, reducers, grammar):
    table = []
    for i in range(len(diagram.T)):
        table.append({})

    for edge in diagram.E:
        if edge.symbol in grammar.nonTerminals:
            table[edge.start][edge.symbol] = Action("goto", edge.end)
        else:
            table[edge.start][edge.symbol] = Action("shift", edge.end)
    for r in reducers:
        for terminal in grammar.terminals:
            table[r.state][terminal] = Action("reduce", r.ruleNum)
    for state in diagram.T:
        if (not state.rules[0].isLast()) and state.rules[0].getNextSymbol() == "$":
            table[state.id]["$"] = Action("accept", 0)

    return table


def printNumber(number):
    if number < 10:
        return str(number) + "  "
    return str(number) + " "


def printTable(table, grammar):
    header = "    "
    for terminal in grammar.terminals:
        header += terminal + "   "
    for nonTerminal in grammar.nonTerminals:
        header += nonTerminal + "   "
    print(header)

    for i in range(len(table)):
        row = ""
        row += str(i)
        if i < 10:
            row += "   "
        else:
            row += "  "
        for terminal in grammar.terminals:

            if terminal not in table[i]:
                row += "    "
            else:
                action = table[i][terminal]
                if action.type == "shift":
                    row += "s" + printNumber(action.number)
                elif action.type == "reduce":
                    row += "r" + printNumber(action.number)
                elif action.type == "accept":
                    row += "a   "
        for nonTerminal in grammar.nonTerminals:
            if nonTerminal not in table[i]:
                row += "    "
            else:
                action = table[i][nonTerminal]
                row += "g" + printNumber(action.number)

        print(row)


def parseSentence(table, sentence, grammar):
    state = 0
    stack = [Stackelement("", 0, None)]
    inputS = list(sentence)
    counter = 0
    while counter < len(inputS):
        if inputS[counter] not in table[state]:
            return None
        else:
            action = table[state][inputS[counter]]
            if action.type == "shift":
                stack.append(Stackelement(inputS[counter], action.number, Element(inputS[counter], counter)))
                counter += 1
                state = action.number
            elif action.type == "reduce":
                rule = grammar.rules[action.number]
                children = []
                end = stack[-1].tree.end

                for i in range(len(rule.symbols)):
                    stackElement = stack.pop()
                    children.append(stackElement.tree)

                if stack[-1].tree is None:
                    start = 0
                else:
                    start = stack[-1].tree.end

                children.reverse()
                state = stack[-1].state
                action2 = table[state][rule.result]
                newNode = Node(rule.result, children, start, end)
                stack.append(Stackelement(rule.result, action2.number, newNode))
                state = action2.number

            elif action.type == "accept":
                return stack.pop().tree
    return None


def constructTable(grammar):
    # State diagram construction
    diagram = Diagram(grammar)

    # Get the reduce set
    reducers = generateReducers(diagram)

    # Generate table
    return generateTable(diagram, reducers, grammar)


def parseSentences(table, grammar, sentences):
    parserTrees = []
    for sentence in sentences:
        parserTrees.append(parseSentence(table, sentence, grammar))
    return parserTrees


def main():
    grammarName = sys.argv[1]
    inputURL = "../../Grammars/"+grammarName+".txt"
    sentenceURL = "../../Grammars/Sentences/"+grammarName+".txt"

    # Read the grammar from the file
    grammar = Grammar(inputURL)

    # Read sentences from file
    sentences = readSentences(sentenceURL)

    # Construct table
    table = constructTable(grammar)

    # Output table
    printTable(table, grammar)

    # Parse sentences
    parserTrees = parseSentences(table, grammar, sentences)

    # Output parsed sentences
    counter = 1
    for tree in parserTrees:
        if tree is None:
            print("Sentence " + str(counter) + ": Not valid")
        else:
            print("Sentence " + str(counter) + ": Valid")
            tree.print1("", "")
        counter += 1

    # Time measurements

    start = time.time_ns()
    for i in range(1000):
        constructTable(grammar)
    end = time.time_ns()
    print("Time to generate table (x1000): " + str((end - start) / pow(10, 9)))

    start = time.time_ns()
    for i in range(1000):
        parseSentences(table, grammar, sentences)
    end = time.time_ns()
    print("Time to parse sentences (x1000): " + str((end - start) / pow(10, 9)))


if __name__ == "__main__":
    main()
