package cryptography

import java.util.*

class CaesarCipher {

    private val alphabet = "abcdefghijklmnopqrstuvwxyz ".toCharArray()

    fun decrypt(message : String) : List<String> {
        val messageLower = message.lowercase(Locale.getDefault())
        val decryptedMessages : MutableList<String> = mutableListOf("")
        for (rotation : Int in 0 until this.alphabet.size) {
            var decryptedMessage = ""
            for (letter: Char in messageLower) {
                val index: Int = this.alphabet.indexOf(letter)
                var newIndex = (index - rotation)
                if (newIndex < 0) {
                    newIndex += this.alphabet.size
                }
                val newLetter = this.alphabet[newIndex]
                decryptedMessage += newLetter
            }
            decryptedMessages.add(decryptedMessage)
        }
        return decryptedMessages
    }

}