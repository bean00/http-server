package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestParserTest {

    @Test
    public void getRequestTarget_getsTheTargetFromTheRequestLine() {
        String requestLine = "Get / HTTP/1.1";
        String expectedTarget = "/";
        RequestParser parser = new RequestParser();

        String target = parser.getRequestTarget(requestLine);

        assertEquals(expectedTarget, target);
    }

}
