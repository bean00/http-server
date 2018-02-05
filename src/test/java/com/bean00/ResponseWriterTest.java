package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseWriterTest {

    @Test
    public void writeResponse_writesASimple200Response_when200IsPassedIn() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Response response = new Response(200);
        StringWriter stringWriter = new StringWriter();
        ResponseWriter responseWriter = new ResponseWriter(stringWriter);

        responseWriter.writeResponse(response);
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
        ResponseWriter responseWriter = new ResponseWriter(stringWriter);

        responseWriter.writeResponse(response);
        String responseString = stringWriter.toString();

        assertEquals(expectedResponse, responseString);
    }

}

