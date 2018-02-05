package com.bean00;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestProcessorTest {
    private Request GETRequest = new Request("GET", "/file1", new ArrayList<>());
    private Request HEADRequest = new Request("HEAD", "/file1", new ArrayList<>());
    private Request invalidHEADRequest = new Request("HEAD", "/foobar", new ArrayList<>());
    private RequestProcessor requestProcessor;

    @BeforeEach
    public void setup() {
        requestProcessor = new RequestProcessor();
    }

    @Test
    public void processRequest_returns200_ifTheURLIsValid() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(GETRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsFileContents() throws IOException {
        String expectedBodyAsAString = "file1 contents";
        byte[] expectedBody = expectedBodyAsAString.getBytes();

        Response response = requestProcessor.processRequest(GETRequest);
        byte[] body = response.getBody();

        assertArrayEquals(expectedBody, body);
    }

    @Test
    public void processRequest_returnsContentLength() throws IOException {
        int expectedContentLength = 14;

        Response response = requestProcessor.processRequest(GETRequest);
        int contentLength = response.getHeader("Content-Length");

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returns404_ifTheURLIsInvalid_forHEADRequest() throws IOException {
        int expectedStatusCode = 404;

        Response response = requestProcessor.processRequest(invalidHEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_displaysContentLength_butNoBody_forHEAD() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n";

        Response response = requestProcessor.processRequest(HEADRequest);
        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

}
