package com.bean00;

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
    public void badRequestHttpException_hasAMessage() {
        RuntimeException exception = new BadRequestHttpException();

        assertEquals("Bad request", exception.getMessage());
    }

}
