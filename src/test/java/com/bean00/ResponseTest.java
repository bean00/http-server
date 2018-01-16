package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    public void toString_returnsASimple200Response() {
        String expectedResponse = "HTTP/1.1 200 OK\r";
        Response response = new Response(200);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_returnsASimple404Response() {
        String expectedResponse = "HTTP/1.1 404 Not Found\r";
        Response response = new Response(404);

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

}
