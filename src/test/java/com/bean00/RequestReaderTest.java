package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestReaderTest {

    @Test
    public void getRequestLine_returnsALine() throws IOException {
        String expectedRequestLine = "Get / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(expectedRequestLine.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        RequestReader reader = new RequestReader(in);

        String requestLine = reader.getRequestLine();

        assertEquals(expectedRequestLine, requestLine);
    }

    @Test
    public void getHeaders_returnsAllLinesBeforeTheBlankLine() throws IOException {
        String[] expectedHeadersAsArray = {
                "Host: localhost:5000",
                "Connection: Keep-Alive"
        };
        ArrayList<String> expectedHeaders =
                new ArrayList<>(Arrays.asList(expectedHeadersAsArray));
        String rawHeaders =
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(rawHeaders.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        RequestReader reader = new RequestReader(in);

        ArrayList<String> headers = reader.getHeaders();

        assertEquals(expectedHeaders, headers);
    }

}
