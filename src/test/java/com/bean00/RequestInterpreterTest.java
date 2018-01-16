package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestInterpreterTest {

    @Test
    public void chooseStatusCode_selects200WhenAValidTargetIsPassedIn() {
        String target = "/";
        RequestInterpreter interpreter = new RequestInterpreter();

        int statusCode = interpreter.chooseStatusCode(target);

        assertEquals(200, statusCode);
    }

    @Test
    public void chooseStatusCode_selects404WhenAnInvalidTargetIsPassedIn() {
        String target = "/foobar";
        RequestInterpreter interpreter = new RequestInterpreter();

        int statusCode = interpreter.chooseStatusCode(target);

        assertEquals(404, statusCode);
    }

}
