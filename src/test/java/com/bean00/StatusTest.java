package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    @Test
    public void getMessage_getsTheAppropriateMessage_forA404StatusCode() {
        String expectedMessage = "Not Found";

        String message = Status.getMessage(404);

        assertEquals(expectedMessage, message);
    }

}
