package com.bean00;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessageControllerTest {

    @Test
    public void readRequest_readsTheRequest() throws IOException {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        PrintWriter out = Mockito.mock(PrintWriter.class);
        Mockito.when(in.readLine()).thenReturn(
                "GET / HTTP/1.1",
                "Host: localhost:5000",
                "Connection: Keep-Alive",
                "");
        MessageController messageController = new MessageController(in, out);

        messageController.readRequest();

        verify(in, times(4)).readLine();
    }

    @Test
    public void readRequest_returnsTheRequestLine() throws IOException {
        String simpleGETRequest =
                "Get / HTTP/1.1\r\n" +
                "\r\n";
        String expectedRequestLine = "Get / HTTP/1.1";
        StringWriter stringWriter = new StringWriter();
        MessageController messageController = createMessageController(
                simpleGETRequest, stringWriter);
        Request request = messageController.readRequest();

        String requestString = request.getRequestLine();

        assertEquals(expectedRequestLine, requestString);
    }

    @Test
    public void interpretRequest_choosesAStatusCodeBasedOnTheTarget() {
        String requestLine = "Get / HTTP/1.1";
        int expectedStatusCode = 200;
        BufferedReader in = Mockito.mock(BufferedReader.class);
        PrintWriter out = Mockito.mock(PrintWriter.class);
        List<String> headers = new ArrayList<>();
        Request request = new Request(requestLine, headers);
        MessageController messageController = new MessageController(in, out);

        Response response = messageController.interpretRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void writeResponse_writesASimple200Response_when200IsPassedIn() {
        String dummyRequest = "";
        String simple200Response = "HTTP/1.1 200 OK\r\n";
        StringWriter stringWriter = new StringWriter();
        Response response = new Response(200);
        MessageController messageController = createMessageController(
                dummyRequest, stringWriter);

        messageController.writeResponse(response);
        String responseString = stringWriter.toString();

        assertEquals(simple200Response, responseString);
    }

    @Test
    public void writeResponse_writesASimple404Response_when404IsPassedIn() {
        String dummyRequest = "";
        String simple404Response = "HTTP/1.1 404 Not Found\r\n";
        StringWriter stringWriter = new StringWriter();
        Response response = new Response(404);
        MessageController messageController = createMessageController(
                dummyRequest, stringWriter);

        messageController.writeResponse(response);
        String responseString = stringWriter.toString();

        assertEquals(simple404Response, responseString);
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
