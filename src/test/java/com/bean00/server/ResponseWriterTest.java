package com.bean00.server;

import com.bean00.httpmessages.HttpHeaders;
import com.bean00.httpmessages.Method;
import com.bean00.httpmessages.Response;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseWriterTest {

    @Test
    public void writeResponse_writesASimple200Response_when200IsPassedIn() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Response response = new Response(200);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(byteArrayOutputStream);

        responseWriter.writeResponse(response);
        String responseString = byteArrayOutputStream.toString();

        assertEquals(expectedResponse, responseString);
    }

    @Test
    public void writeResponse_writesAFullResponse_withTextContent() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n" +
                "file1 contents";
        String[][] rawHeaders = {{"Content-Length", "14"}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);
        String body = "file1 contents";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, Method.GET, headers, rawBody);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(byteArrayOutputStream);

        responseWriter.writeResponse(response);
        String responseString = byteArrayOutputStream.toString();

        assertEquals(expectedResponse, responseString);
    }

}
