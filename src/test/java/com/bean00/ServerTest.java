package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private String simple200Response = "HTTP/1.1 200 OK\r\n";
    private String simple404Response = "HTTP/1.1 404 Not Found\r\n";
    private String simple500Response = "HTTP/1.1 500 Internal Server Error\r\n";

    @Test
    public void run_respondWith200_withAValidTarget() {
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                requestWithAValidURL, stringWriter);
        Server server = new Server();

        server.run(messageController);
        String response = stringWriter.toString();

        assertEquals(simple200Response, response);
    }

    @Test
    public void run_respondsWith404_ifTargetNotFound() {
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                requestWithAMissingURL, stringWriter);
        Server server = new Server();

        server.run(messageController);
        String response = stringWriter.toString();

        assertEquals(simple404Response, response);
    }

    @Test
    public void run_respondsCorrectly_toTwoGetRequests() {
        String twoRequests = requestWithAValidURL + requestWithAMissingURL;
        String expectedResponses = simple200Response + simple404Response;
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                twoRequests, stringWriter);
        Server server = new Server();

        server.run(messageController);
        server.run(messageController);
        String response = stringWriter.toString();

        assertEquals(expectedResponses, response);
    }

    @Test
    public void run_respondsWith500_whenThereIsANonHandledError() {
        String dummyRequest = "";
        InputStream inputStream = new ByteArrayInputStream(dummyRequest.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);
        MessageController messageController = new ServerErrorMessageController(in, out);
        Server server = new Server();

        server.run(messageController);
        String response = stringWriter.toString();

        assertEquals(simple500Response, response);
    }

    private MessageController createMessageController(String request,
                                                      StringWriter stringWriter) {
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(stringWriter);
        MessageController messageController = new MessageController(in, out);

        return messageController;
    }

}
