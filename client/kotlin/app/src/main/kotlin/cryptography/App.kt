
package cryptography

fun main(args : Array<String>) {
    val clientIp = args[0]
    val clientPort = args[1]
    val client = createClient(clientIp, clientPort)
    val encryptedMessage = client.sendDefaultMessage()
    val decryptedMessages = decryptMessage(encryptedMessage)
    printDecryptedMessages(decryptedMessages)
    var key = checkWordWithPons(decryptedMessages)
    val decide = readInput("Machine: $key\nAccept? y/n")
    if (decide == "n") {
        key = readInput("Please Select The Key")
    }
    client.sendKey(key)
    client.closeClient()
}

fun createClient(client_ip: String, client_port: String): Client {
    return Client(client_ip, client_port)
}

fun decryptMessage(message: String): List<String> {
    val caesarCipher = CaesarCipher()

    return caesarCipher.decrypt(message)
}

fun checkWordWithPons(decryptedMessages: List<String>) : String {
    // TODO ERROR With Pons (probably api does not work)
    val dictionary = Dictionary()
    var maxValue = -1
    var maxIndex = -1
    for (index in decryptedMessages.indices) {
        val newValue = dictionary.pons(decryptedMessages[index])
        if (newValue > maxValue) {
            maxValue = newValue
            maxIndex = index
        }
    }

    return (maxIndex -1).toString()
}

fun readInput(message : String): String {
    var isNoValidChoice = true

    var userChoice = ""

    while(isNoValidChoice) {
        println(message)
        val userInput = readLine()
        if (userInput != null) {
            isNoValidChoice = false
            userChoice = userInput
        }

        if (isNoValidChoice) {
            println("Please Enter A Valid Input")
        }
    }

    return userChoice
}

fun printDecryptedMessages(decryptedMessages: List<String>) {
    for (counter : Int in 1 until decryptedMessages.size) {
        println("${(if(counter <= 10) " " else "") + (counter-1)} : ${decryptedMessages[counter]}")
    }
}
