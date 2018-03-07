package com.bean00.httpexception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadRequestHttpExceptionTest {

    @Test
    public void badRequestHttpException_canBeThrown() {
        assertThrows(BadRequestHttpException.class, () -> {
            throw new BadRequestHttpException();
        });
    }

    @Test
    public void badRequestHttpException_hasAnEmptyMessage_forTheDefaultConstructor() {
        String errorMessage = "";
        RuntimeException exception = new BadRequestHttpException();

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void badRequestHttpException_hasTheMessage_thatWasPassedIn() {
        String errorMessage = "Custom bad request message";
        RuntimeException exception = new BadRequestHttpException(errorMessage);

        assertEquals("Custom bad request message", exception.getMessage());
    }

}
