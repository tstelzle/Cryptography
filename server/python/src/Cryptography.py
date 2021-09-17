import random


class Cryptography:

    def __init__(self):
        self.alphabet = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                         'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ']
        self.key = self.generate_new_key()
        self.scrambled_alphabet = self.generate_new_scrambled_alphabet()

    def create_cypher(self) -> str:
        # TODO Debug for now
        decider = random.randint(0, 1)
        # decider = 1
        if decider == 1:
            cypher = self.create_caeser_cypher()
        else:
            cypher = self.create_scrambled_cypher()

        return cypher

    def generate_new_key(self) -> int:
        return random.randint(0, len(self.alphabet))

    def generate_new_scrambled_alphabet(self) -> [str]:
        return random.sample(self.alphabet, len(self.alphabet))

    def check_caesar_result(self, key: int) -> str:
        if key == self.key:
            self.key = self.generate_new_key()
            return "Key Correct!"
        else:
            return "Key Wrong!"

    def check_scrambled_result(self, message: str) -> str:
        if list(message) == self.scrambled_alphabet:
            self.scrambled_alphabet = self.generate_new_scrambled_alphabet()
            return "Alphabet Correct!"
        else:
            return "Alphabet Incorrect!"

    def create_caeser_cypher(self) -> str:
        message = self.get_text()
        cypher = ""
        for letter in message:
            index = self.alphabet.index(letter)
            cypher += self.alphabet[(index + self.key) % len(self.alphabet)]

        print("New Caesar Cypher:", cypher)
        return cypher

    @staticmethod
    def get_text() -> str:
        faust_file = open("../../faust", "r")
        lines = [line.strip() for line in faust_file.readlines()]

        return random.sample(lines, 1)[0]

    def create_scrambled_cypher(self):
        message = self.get_text()
        cypher = ""
        for letter in message:
            index = self.alphabet.index(letter)
            cypher += self.scrambled_alphabet[index]

        print("New Scrambled Cypher:", cypher)
        return cypher
