package com.bean00;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    public void getRequestLine_getsRequestLine() {
        String expectedRequestURL = "/";
        List<String> headers = Arrays.asList(
                "Host: localhost:5000",
                "Connection: Keep-Alive"
        );

        Request request = new Request(expectedRequestURL, headers);
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

}
