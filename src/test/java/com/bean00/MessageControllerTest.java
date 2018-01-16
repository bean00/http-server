package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageControllerTest {

    @Test
    public void getRequest_returnsARequest_thatHasTheRequestURL() throws IOException {
        String expectedRequestURL = "/";
        String simpleGETRequest =
                "GET / HTTP/1.1\r\n" +
                "\r\n";
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                simpleGETRequest, stringWriter);

        Request request = messageController.getRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

    @Test
    public void interpretRequest_choosesAStatusCodeBasedOnTheRequestURL() {
        int expectedStatusCode = 200;
        String dummyRequest = "";
        String requestURL = "/";
        List<String> headers = new ArrayList<>();
        Request request = new Request(requestURL, headers);
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                dummyRequest, stringWriter);

        Response response = messageController.interpretRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void writeResponse_writesASimple200Response_when200IsPassedIn() {
        String expectedResponse = "HTTP/1.1 200 OK\r\n";
        String dummyRequest = "";
        Response response = new Response(200);
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                dummyRequest, stringWriter);

        messageController.writeResponse(response);
        String responseString = stringWriter.toString();

        assertEquals(expectedResponse, responseString);
    }

    @Test
    public void writeResponse_writesASimple404Response_when404IsPassedIn() {
        String expectedResponse = "HTTP/1.1 404 Not Found\r\n";
        String dummyRequest = "";
        Response response = new Response(404);
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                dummyRequest, stringWriter);

        messageController.writeResponse(response);
        String responseString = stringWriter.toString();

        assertEquals(expectedResponse, responseString);
    }

    private MessageController createMessageController(String request,
                                                      StringWriter stringWriter) {
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(stringWriter);
        MessageController messageController = new MessageController(in, out);

        return messageController;
    }

}
