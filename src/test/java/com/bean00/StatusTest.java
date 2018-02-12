package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    @Test
    public void getMessage_getsTheCorrectMessage_forA_200_status() {
        String expectedMessage = "OK";

        String message = Status.getMessage(200);

        assertEquals(expectedMessage, message);
    }

    @Test
    public void getMessage_getsTheCorrectMessage_forA_400_status() {
        String expectedMessage = "Bad Request";

        String message = Status.getMessage(400);

        assertEquals(expectedMessage, message);
    }

    @Test
    public void getMessage_getsTheCorrectMessage_forA_404_status() {
        String expectedMessage = "Not Found";

        String message = Status.getMessage(404);

        assertEquals(expectedMessage, message);
    }

    @Test
    public void getMessage_getsTheCorrectMessage_forA_500_status() {
        String expectedMessage = "Internal Server Error";

        String message = Status.getMessage(500);

        assertEquals(expectedMessage, message);
    }

}
