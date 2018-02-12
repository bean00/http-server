package com.bean00;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerTest {
    private String simple200Response =
            "HTTP/1.1 200 OK\r\n" +
            "\r\n";

    private Server server;
    private RequestParser parser;
    private RequestProcessor processor;
    private OutputStream byteArrayOutputStream;
    private ResponseWriter writer;
    private Request request;

    @BeforeEach
    public void setup() throws IOException {
        server = new Server();
        parser = mock(RequestParser.class);
        processor = mock(RequestProcessor.class);
        byteArrayOutputStream = new ByteArrayOutputStream();
        writer = new ResponseWriter(byteArrayOutputStream);

        request = new Request("", "", new ArrayList<>());
        when(parser.parseRequest()).thenReturn(request);
        when(processor.processRequest(request)).thenReturn(new Response(200));
    }

    @Test
    public void run_respondWith200_withAValidTarget() throws IOException {
        server.run(parser, processor, writer);
        String response = byteArrayOutputStream.toString();

        assertEquals(simple200Response, response);
    }

    @Test
    public void run_respondsCorrectly_toTwoGetRequests() throws IOException {
        String expectedResponses = simple200Response + simple200Response;

        server.run(parser, processor, writer);
        server.run(parser, processor, writer);
        String response = byteArrayOutputStream.toString();

        assertEquals(expectedResponses, response);
    }

    @Test
    public void run_respondsWith400_whenTheRequestIsBad() throws IOException {
        String simple400Response =
                "HTTP/1.1 400 Bad Request\r\n" +
                "\r\n";
        when(parser.parseRequest()).thenThrow(new BadRequestHttpException());

        server.run(parser, processor, writer);
        String response = byteArrayOutputStream.toString();

        assertEquals(simple400Response, response);
    }

    @Test
    public void run_respondsWith500_whenThereIsANonHandledError() throws IOException {
        String simple500Response =
                "HTTP/1.1 500 Internal Server Error\r\n" +
                "\r\n";
        when(processor.processRequest(request)).thenThrow(new RuntimeException());

        server.run(parser, processor, writer);
        String response = byteArrayOutputStream.toString();

        assertEquals(simple500Response, response);
    }

}
