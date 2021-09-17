package cryptography

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class Client(private val client_ip: String, private val client_port: String) {

    private val client : Socket = Socket(this.client_ip, this.client_port.toInt())
    private val output : PrintWriter = PrintWriter(this.client.getOutputStream(), true)
    private val input : BufferedReader = BufferedReader(InputStreamReader(this.client.inputStream))

    fun sendMessage(message : String) : String {
        this.output.print(message + "\r\n")
        this.output.flush()
        println("Send Message $message")
        return this.receive()
    }

    fun sendDefaultMessage() : String {
        this.output.print("message\r\n")
        this.output.flush()
        println("Send Default Message")
        return this.receive()
    }

    fun sendKey(key : String) : String {
        this.output.print("key $key")
        this.output.flush()
        return this.receive()
    }

    private fun receive() : String {
        val receivedMessage = this.input.readLine()
        println("Received $receivedMessage")
        return receivedMessage
    }

    fun closeClient() {
        this.output.close()
        this.input.close()
        this.client.close()
    }

}