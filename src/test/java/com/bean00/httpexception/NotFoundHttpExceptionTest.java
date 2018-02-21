package com.bean00.httpexception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotFoundHttpExceptionTest {

    @Test
    public void notFoundHttpException_canBeThrown() {
        assertThrows(NotFoundHttpException.class, () -> {
            throw new NotFoundHttpException();
        });
    }

    @Test
    public void notFoundHttpException_hasAMessage() {
        RuntimeException exception = new NotFoundHttpException();

        assertEquals("Not found", exception.getMessage());
    }

}
