from abc import ABC, abstractmethod


class ParserTree(ABC):
    @abstractmethod
    def print1(self, prefix, continuation):
        pass
