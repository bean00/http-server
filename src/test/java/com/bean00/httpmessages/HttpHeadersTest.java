package com.bean00.httpmessages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpHeadersTest {

    @Test
    public void headerExists_returnsFalse_ifTheHeaderDoesNotExist() {
        HttpHeaders headers = new HttpHeaders();

        boolean exists = headers.headerExists("Content-Length");

        assertFalse(exists);
    }

    @Test
    public void headerExists_returnsTrue_ifTheHeaderExists() {
        String[][] rawHeaders = {{"Content-Length", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        boolean exists = headers.headerExists("Content-Length");

        assertTrue(exists);
    }

    @Test
    public void headerExists_returnsTrue_ifTheCapitalizationOfHeaders_doesNotMatch() {
        String[][] rawHeaders = {{"Content-Length", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        boolean exists = headers.headerExists("CONTENT-LENGTH");

        assertTrue(exists);
    }

    @Test
    public void setHeader_addsAHeader_ifThereAreNotAnyHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setHeader("Content-Length", "1");
        boolean exists = headers.headerExists("Content-Length");

        assertTrue(exists);
    }

    @Test
    public void setHeader_overwritesTheFieldValue_ifTheHeaderAlreadyExists() {
        String expectedValue = "8";
        String[][] rawHeaders = {{"Content-Length", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        headers.setHeader("Content-Length", "8");
        String value = headers.getHeader("Content-Length");

        assertEquals(expectedValue, value);
    }

    @Test
    public void setHeader_capitalizesTheFirstLetterOfEachWord_beforeSettingTheHeader() {
        String expectedValue = "1";
        HttpHeaders headers = new HttpHeaders();

        headers.setHeader("CONTENT-LENGTH", "1");
        String value = headers.getHeader("Content-Length");

        assertEquals(expectedValue, value);
    }

    @Test
    public void getHeader_returnsTheFieldValue_forAFieldName() {
        String expectedValue = "1";
        String[][] rawHeaders = {{"Content-Length", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        String value = headers.getHeader("Content-Length");

        assertEquals(expectedValue, value);
    }

    @Test
    public void getHeader_isCaseInsensitive_forHeadersPassedIn() {
        String expectedValue = "1";
        String[][] rawHeaders = {{"CONTENT-LENGTH", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        String value = headers.getHeader("Content-Length");

        assertEquals(expectedValue, value);
    }

    @Test
    public void getHeader_isCaseInsensitive_whenGettingHeaders() {
        String expectedValue = "1";
        String[][] rawHeaders = {{"Content-Length", "1"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        String value = headers.getHeader("CONTENT-LENGTH");

        assertEquals(expectedValue, value);
    }

    @Test
    public void toString_returnsAnEmptyString_whenNoHeadersArePassedIn() {
        String expectedHeaderString = "";
        HttpHeaders headers = new HttpHeaders();

        String headerString = headers.toString();

        assertEquals(expectedHeaderString, headerString);
    }

    @Test
    public void toString_returnsTheHeadersAsAString_whenHeadersArePassedIn() {
        String expectedHeaderString =
                "Content-Length: 1\r\n" +
                "Content-Type: text/plain\r\n";
        String[][] rawHeaders = {
                {"Content-Length", "1"},
                {"Content-Type", "text/plain"}
        };
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        String headerString = headers.toString();

        assertEquals(expectedHeaderString, headerString);
    }

}
