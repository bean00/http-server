package com.bean00;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RequestParserTest {

    @Test
    public void parseRequest_callsReadLineTheCorrectNumberOfTimes() throws IOException {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        Mockito.when(in.readLine()).thenReturn(
                "GET / HTTP/1.1",
                "Host: localhost:5000",
                "Connection: Keep-Alive",
                "");
        RequestParser parser = new RequestParser(in);

        parser.parseRequest();

        verify(in, times(4)).readLine();
    }

    @Test
    public void parseRequest_createsARequest_thatHasARequestMethod() throws IOException {
        String expectedRequestMethod = "GET";
        String getRequest =
                "GET / HTTP/1.1\r\n" +
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(getRequest.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        RequestParser parser = new RequestParser(in);

        Request request = parser.parseRequest();
        String requestMethod = request.getRequestMethod();

        assertEquals(expectedRequestMethod, requestMethod);
    }

    @Test
    public void parseRequest_createsARequest_thatHasARequestURL() throws IOException {
        String expectedRequestURL = "/";
        String getRequest =
                "GET / HTTP/1.1\r\n" +
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(getRequest.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        RequestParser parser = new RequestParser(in);

        Request request = parser.parseRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

}
