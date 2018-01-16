package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    @Test
    public void handleGET_respondWith200_withAValidTarget() throws IOException {
        String sampleGETRequest =
                "Get / HTTP/1.1\r\n"+
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n" +
                "\r\n";
        String simple200Response = "HTTP/1.1 200 OK\r\n";
        InputStream inputStream = new ByteArrayInputStream(sampleGETRequest.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);
        Server server = new Server(in, out);

        server.handleGET();
        String response = stringWriter.toString();

        assertEquals(simple200Response, response);
    }

    @Test
    public void handleGET_respondsWith404_ifTargetNotFound() throws IOException {
        String sampleGETRequest =
                "Get /foobar HTTP/1.1\r\n" +
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n" +
                "\r\n";
        String simple404Response = "HTTP/1.1 404 Not Found\r\n";
        InputStream inputStream = new ByteArrayInputStream(sampleGETRequest.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);
        Server server = new Server(in, out);

        server.handleGET();
        String response = stringWriter.toString();

        assertEquals(simple404Response, response);
    }

}
