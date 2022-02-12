Grammar files are txt containing the following:
-First line, introduces the non-termionals symbols, except for the initial symbol R, the symbols need to be separated by a space. Recommended to use capitalized letters only.

-Second line, introduces the terminal symbols, separated by a space. Recommended to use lower-case letters, numbers and symbols. Each terminal symbol should use a single character.

Don't use R,$ or space as symbols as they are reserved.

-Rest of the lines, one line for rules, witht eh following structure:
{non-terminal symbol}→{rule}
There can only be one rule with R, and has to be the first rule.

Example: S→(L)

The $ symbol represents the EOS


Full grammar example:
S L
x ( ) ,
R→S$
S→(L)
S→x
L→S
L→L,S



