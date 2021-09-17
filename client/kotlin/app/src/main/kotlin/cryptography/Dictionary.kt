package cryptography

import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration


class Dictionary {

    // TODO get api authentication
    // https://api.duden.io/#authentication
    fun duden(word : String) {
        println(word)
        var url = URL("https://api.duden.de/v1/spellcheck?text=$word")
        println(url)
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"  // optional default is GET
            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                }
            }
        }
    }

    fun pons(sentence : String) : Int {
        var words = sentence.split(" ")
        var acceptedRequests = 0
        for (word in words) {
            val httpClient: HttpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build()
            val requestHead = HttpRequest.newBuilder()
                .header("X-Secret", "6d2dd6735e1fe074672b8195b9512a0fe1e5380004c40aef054b6ef19a97b38d")
                .uri(URI.create("https://api.pons.com/v1/dictionary?q=$word&l=deen"))
                .build()
            val httpResponse = httpClient.send(requestHead, HttpResponse.BodyHandlers.discarding())
            var statusCode = httpResponse.statusCode()
            if (statusCode == 200) {
                acceptedRequests += 1
            }
        }

        return acceptedRequests
    }
}