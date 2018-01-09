package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    public void simple200Response_returnsASimple200Response() {
        Response response = new Response();
        String simple200Response = "HTTP/1.1 200 OK\r";

        String actualResponse = response.simple200Response();

        assertEquals(simple200Response, actualResponse);
    }

}
