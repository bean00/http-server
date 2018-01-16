package com.bean00;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageControllerTest {
    private Request request = new Request("/file1", new ArrayList<>());
    private MessageController messageController;

    @BeforeEach
    public void setup() {
        messageController = new MessageController();
    }

    @Test
    public void processRequest_returns200_ifTheURLIsValid() throws IOException {
        int expectedStatusCode = 200;

        Response response = messageController.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsFileContents() throws IOException {
        String expectedBodyAsAString = "file1 contents";
        byte[] expectedBody = expectedBodyAsAString.getBytes();

        Response response = messageController.processRequest(request);
        byte[] body = response.getBody();

        assertArrayEquals(expectedBody, body);
    }

    @Test
    public void processRequest_returnsContentLength() throws IOException {
        int expectedContentLength = 14;

        Response response = messageController.processRequest(request);
        int contentLength = response.getHeader("Content-Length");

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void writeResponse_writesASimple200Response_when200IsPassedIn() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Response response = new Response(200);
        StringWriter stringWriter = new StringWriter();

        messageController.writeResponse(response, stringWriter);
        String responseString = stringWriter.toString();

        assertEquals(expectedResponse, responseString);
    }

    @Test
    public void writeResponse_writesASimple404Response_when404IsPassedIn() throws IOException {
        String expectedResponse =
                "HTTP/1.1 404 Not Found\r\n" +
                "\r\n";
        Response response = new Response(404);
        StringWriter stringWriter = new StringWriter();

        messageController.writeResponse(response, stringWriter);
        String responseString = stringWriter.toString();

        assertEquals(expectedResponse, responseString);
    }

}
