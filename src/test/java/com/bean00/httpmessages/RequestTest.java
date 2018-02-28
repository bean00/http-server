package com.bean00.httpmessages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    public void toString_returnsTheRequestLine_forASimpleGETRequest() {
        String expectedRequestString =
                "GET / HTTP/1.1\r\n" +
                "\r\n";
        Request request = new Request("GET", "/");

        String requestString = request.toString();

        assertEquals(expectedRequestString, requestString);
    }

    @Test
    public void toString_returnsTheHeaders_forAGETRequest() {
        String expectedRequestString =
                "GET / HTTP/1.1\r\n" +
                "Accept: text/html\r\n" +
                "\r\n";
        String[][] rawHeaders = {{"Accept", "text/html"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);
        Request request = new Request("GET", "/", headers);

        String requestString = request.toString();

        assertEquals(expectedRequestString, requestString);
    }

    @Test
    public void toString_returnsTheBody_forAPUTRequest() {
        String expectedRequestString =
                "PUT /new.html HTTP/1.1\r\n" +
                "Content-Length: 16\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<p>New File</p>\n";
        String[][] rawHeaders = {
                {"Content-Type", "text/html"},
                {"Content-Length", "16"}
        };
        HttpHeaders headers = new HttpHeaders(rawHeaders);
        String body = "<p>New File</p>\n";
        Request request = new Request("PUT", "/new.html", headers, body);

        String requestString = request.toString();

        assertEquals(expectedRequestString, requestString);
    }

    @Test
    public void getHeader_getsTheFieldValue_forAFieldName() {
        String expectedFieldValue = "text/html";
        String[][] rawHeaders = {{"Accept", "text/html"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);
        Request request = new Request("GET", "/", headers);

        String fieldValue = request.getHeader("Accept");

        assertEquals(expectedFieldValue, fieldValue);
    }

    @Test
    public void getBodyAsBytes_returnsTheBody_asAByteArray() {
        byte[] expectedBody = "body".getBytes();
        Request request = new Request("PUT", "/", new HttpHeaders(), "body");

        byte[] body = request.getBodyAsBytes();

        assertArrayEquals(expectedBody, body);
    }

}
