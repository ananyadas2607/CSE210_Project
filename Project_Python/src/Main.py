def generateReducers(diagram, grammar):
    reducers=[]
    for state in diagram.T:
        for positionRule in state.rules:
            if positionRule.isLast():
                reducers.append(state.id, positionRule.rule.number)

    return reducers


def readSentences(sentenceURL):
    f=open(sentenceURL,"r")
    sentences=[]

    while f.readline():
        sentences.add(line + "$")
    return sentences


def generateTable(diagram, reducers, grammar):
    table=[]
    i=0
    while i<diagram.T.len():
        table.append(dictionary)
    i+=1

    for edge in diagram.E:
        if edge.symbol in grammar.nonTerminals:
            table(edge.start).put(edge.symbol, Action("goto", edge.end))
        else:
            table(edge.start).put(edge.symbol, Action("shift", e.end))
    for reducers in Reducer:
        for terminal in grammar.terminals:
            table.get(reducers.state).put(terminal, Action("reduce", r.ruleNum))
    for state in diagram.T:
        if not state.rules[0].isLast() and state.rules[0].getNextSymbol().equals("$"):
            table.get(state.id).put("$", Action("accept", 0))

    return table


class Main:
    def __init__(self):
        pass

    def printTable(self, table, grammar):
        header = "   "
        for terminal in grammar.terminals:
            header.append(terminal).append("  ")
        for nonTerminal in grammar.nonTerminals:
            header.append(nonTerminal).append("  ")
        print(header)
        i=0
        while i < table.len() :
            row=""
            row.append(i)
            if i < 10 :
                row.append("  ")
            else:
                row.append("  ")
            for terminal in grammar.terminals :
                action= table[i][terminal]
                if action is None:
                    row.append("   ")
                else:
                    if action.type == "shift":
                        row.append("s").append(action.number).append(" ")
                        break
                    elif action.type == "reduce":
                        row.append("r").append(action.number).append(" ")
                        break
                    elif action.type == "accept":
                        row.append("a"+" ")
                        break
            for nonTerminal in grammar.nonTerminals:
                action=table[i][nonTerminal]
                if action is None:
                    row.append("   ")
                else:
                    row.append("g").append(action.number).append(" ")

            print(row)

    def parseSentence(self, table, sentence, grammar):
        state = 0
        stack.push("", 0, None)
        input = sentence.split("")
        counter = 0
        while counter < input.size() :
            action = table.get(state).get(input[counter])
            if action is None:
                return None
            elif action.type is "shift":
                stack.push(Stackelement[input[counter], action.number, Element[input[counter]], counter])
                counter +=1
                state = action.number
                break
            elif action.type is "reduce":
                rule = grammar.rules[action.number]
                children=[]
                end = stack.peek().tree.end

                i=0
                while i < rule.symbols.size() :
                    stackElement = stack.pop()
                    children.append(stackElement.tree)
                i+=1

                if stack.peek().tree is None:
                    start = 0
                else:
                    return stack.peek().tree.end

                reverse(children)
                state = stack.peek().state
                action2 = table.get(state).get(rule.result)
                newNode = Node(rule.result, children, start, end)
                stack.push(Stackelement(rule.result, action2.number, newNode))
                state = action2.number
                break
            elif action.type is "accept":
                return stack.pop().tree
                break
        return None

    def main(self):
        #String inputURL = args[0]
        inputURL = "/Users/ananya/Documents/GitHub/CSE210_Project/Grammars/Grammar_1.txt"
        sentenceURL = "/Users/ananya/Documents/GitHub/CSE210_Project/Grammars/Sentences/Grammar_1.txt"

        #Read the grammar from the file
        grammar = Grammar(inputURL)

        start = time.time() * 1000

        #State diagram construction
        diagram = Diagram(grammar)

        #Get the reduce set
        reducers = generateReducers(diagram, grammar)

        #Generate table
        table = generateTable(diagram, reducers, grammar)

        #Output table and time elapsed
        printTable(table, grammar)

        #Read sentences from file
        sentences = readSentences(sentenceURL)

        #Parse sentences
        start = time.time() * 1000
        parserTrees = []
        for sentence in sentences :
            parserTrees.add(parseSentence(table, sentence, grammar))

        end = time.time() * 1000

        #Output parsed sentences and time elapsed
        counter = 1
        for tree in parserTrees :
            if tree == null :
                print("Sentence " + counter + ": Not valid")
            else :
                print("Sentence " + counter + ": Valid")
                tree.print(string, "", "")
                print(string)
            counter+=1

            print("Time elapsed: " + (end-start)/1000.0)
            