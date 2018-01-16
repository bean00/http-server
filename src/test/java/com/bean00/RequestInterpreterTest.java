package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestInterpreterTest {

    @Test
    public void chooseStatusCode_returns200WhenTheRequestURLIsFound() {
        String requestURL = "/";
        RequestInterpreter interpreter = new RequestInterpreter();

        int statusCode = interpreter.chooseStatusCode(requestURL);

        assertEquals(200, statusCode);
    }

    @Test
    public void chooseStatusCode_returns404WhenTheRequestURLIsNotFound() {
        String requestURL = "/foobar";
        RequestInterpreter interpreter = new RequestInterpreter();

        int statusCode = interpreter.chooseStatusCode(requestURL);

        assertEquals(404, statusCode);
    }

}
