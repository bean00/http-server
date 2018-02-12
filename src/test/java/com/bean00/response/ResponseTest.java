package com.bean00.response;

import com.bean00.request.Method;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    public void getResponseAsByteArray_returnsASimple200Response() throws IOException {
        String responseAsString =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        byte[] expectedResponse = responseAsString.getBytes();
        Response response = new Response(200);

        byte[] actualResponse = response.getResponseAsByteArray();

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getResponseAsByteArray_returnsAResponseWithFileContents() throws IOException {
        String responseAsString =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n" +
                "file1 contents";
        byte[] expectedResponse = responseAsString.getBytes();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "14");
        String body = "file1 contents";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, Method.GET, headers, rawBody);

        byte[] actualResponse = response.getResponseAsByteArray();

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getResponseAsByteArray_includesContentLength_ifTheBodyIsEmpty() throws IOException {
        String responseAsString =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";
        byte[] expectedResponse = responseAsString.getBytes();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "0");
        String body = "";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, Method.GET, headers, rawBody);

        byte[] actualResponse = response.getResponseAsByteArray();

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getResponseAsByteArray_hasContentLength_butNoBody_forHEADRequest() throws IOException {
        String responseAsString =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n";
        byte[] expectedResponse = responseAsString.getBytes();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "14");
        String body = "file1 contents";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, Method.HEAD, headers, rawBody);

        byte[] actualResponse = response.getResponseAsByteArray();

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getResponseAsString_returnsTheResponse_asAString() throws IOException {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n" +
                "file1 contents";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Length", "14");
        String body = "file1 contents";
        byte[] rawBody = body.getBytes();
        Response response = new Response(200, Method.GET, headers, rawBody);

        String actualResponse = response.getResponseAsString();

        assertEquals(expectedResponse, actualResponse);
    }

}
