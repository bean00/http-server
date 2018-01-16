package com.bean00;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    private String requestWithAValidURL =
            "GET / HTTP/1.1\r\n"+
            "Host: localhost:5000\r\n" +
            "Connection: Keep-Alive\r\n" +
            "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
            "Accept-Encoding: gzip,deflate\r\n" +
            "\r\n";

    private String requestWithAMissingURL =
            "GET /foobar HTTP/1.1\r\n" +
            "Host: localhost:5000\r\n" +
            "Connection: Keep-Alive\r\n" +
            "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
            "Accept-Encoding: gzip,deflate\r\n" +
            "\r\n";

    private String simple200Response =
            "HTTP/1.1 200 OK\r\n" +
            "\r\n";
    private String simple404Response =
            "HTTP/1.1 404 Not Found\r\n" +
            "\r\n";
    private String simple500Response =
            "HTTP/1.1 500 Internal Server Error\r\n" +
            "\r\n";

    private StringWriter writer;
    private MessageController messageController;
    private Server server;

    @BeforeEach
    public void setup() {
        writer = new StringWriter();
        messageController = new MessageController();
        server = new Server();
    }

    @Test
    public void run_respondWith200_withAValidTarget() throws IOException {
        BufferedReader reader = createBufferedReader(requestWithAValidURL);

        server.run(messageController, reader, writer);
        String response = writer.toString();

        assertEquals(simple200Response, response);
    }

    @Test
    public void run_respondsWith404_ifTargetNotFound() throws IOException {
        BufferedReader reader = createBufferedReader(requestWithAMissingURL);

        server.run(messageController, reader, writer);
        String response = writer.toString();

        assertEquals(simple404Response, response);
    }

    @Test
    public void run_respondsCorrectly_toTwoGetRequests() throws IOException {
        String twoRequests = requestWithAValidURL + requestWithAMissingURL;
        String expectedResponses = simple200Response + simple404Response;
        BufferedReader reader = createBufferedReader(twoRequests);

        server.run(messageController, reader, writer);
        server.run(messageController, reader, writer);
        String response = writer.toString();

        assertEquals(expectedResponses, response);
    }

    @Test
    public void run_respondsWith500_whenThereIsANonHandledError() throws IOException {
        BufferedReader reader = createBufferedReader(requestWithAValidURL);
        MessageController messageController = new ServerErrorMessageController();

        server.run(messageController, reader, writer);
        String response = writer.toString();

        assertEquals(simple500Response, response);
    }

    private BufferedReader createBufferedReader(String request) {
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return reader;
    }

}
