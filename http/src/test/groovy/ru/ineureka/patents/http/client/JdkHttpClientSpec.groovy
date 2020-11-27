package ru.ineureka.patents.http.client

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import ru.ineureka.patents.http.client.exception.HttpClientException
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class JdkHttpClientSpec extends Specification {

    @Shared
    def client = new JdkHttpClient()

    @Shared
    def addr = new InetSocketAddress(1234)

    @Shared
    def httpServer = HttpServer.create(addr, 0)

    def setupSpec() {
        httpServer.createContext("/test/post", new HttpHandler() {
            void handle(HttpExchange exchange) throws IOException {
                byte[] response = 'TEST POST OK'.getBytes()
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length)
                exchange.getResponseBody().write(response)
                exchange.close()
            }
        })
        httpServer.createContext("/test/get", new HttpHandler() {
            void handle(HttpExchange exchange) throws IOException {
                byte[] response = 'TEST GET OK'.getBytes()
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length)
                exchange.getResponseBody().write(response)
                exchange.close()
            }
        })
        httpServer.start()
    }

    def cleanupSpec() {
        httpServer.stop(0)
    }

    def "if get call is ok in a positive case"() {
        when:
        def response = client.get('http://localhost:' + addr.getPort() + '/test/get')

        then:
        def result = new String(response.get(), StandardCharsets.UTF_8)

        and:
        result == response.asString(StandardCharsets.UTF_8)
        result == 'TEST GET OK'
    }

    def "if get call is ok in a negative case"() {
        when:
        client.get('garbage://url')

        then:
        thrown(HttpClientException)
    }

    def "if post call is ok in a positive case"() {
        when:
        def response = client.post('http://localhost:' + addr.getPort() + '/test/post', 'something')

        then:
        def result = new String(response.get(), StandardCharsets.UTF_8)

        and:
        result == response.asString(StandardCharsets.UTF_8)
        result == 'TEST POST OK'
    }

    def "if post call is ok in a negative case"() {
        when:
        client.post('garbage://url', 'something')

        then:
        thrown(HttpClientException)
    }
}
