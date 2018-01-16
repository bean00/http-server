package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    public void toString_returnsASimple200Response() {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Response response = new Response(200);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_returnsASimple404Response() {
        String expectedResponse =
                "HTTP/1.1 404 Not Found\r\n" +
                "\r\n";
        Response response = new Response(404);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_returnsASimple500Response() {
        String expectedResponse =
                "HTTP/1.1 500 Internal Server Error\r\n" +
                "\r\n";
        Response response = new Response(500);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_returnsA200ResponseWithFileContents() {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n" +
                "file1 contents";
        String body = "file1 contents";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, rawBody);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_stillAddsANewline_ifTheBodyIsEmpty() {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        String body = "";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, rawBody);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

}
