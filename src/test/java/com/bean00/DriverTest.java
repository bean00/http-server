package com.bean00;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DriverTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void main_printsAnErrorMessage_ifInvalidArgumentsArePassedIn() throws IOException {
        String expectedString = "[ERROR] Invalid arguments passed in";
        String[] args = {};

        Driver.main(args);
        String fullString = outContent.toString();
        boolean containsExpectedString = fullString.contains(expectedString);

        assertTrue(containsExpectedString);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

}
