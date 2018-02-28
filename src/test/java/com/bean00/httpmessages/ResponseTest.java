package com.bean00.httpmessages;

import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        Response response = buildResponse(200, Method.GET, 14, "file1 contents");

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
        Response response = buildResponse(200, Method.GET, 0, "");

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
        Response response = buildResponse(200, Method.HEAD, 14, "file1 contents");

        byte[] actualResponse = response.getResponseAsByteArray();

        assertArrayEquals(expectedResponse, actualResponse);
    }

    @Test
    public void toString_returnsTheResponse_asAString() {
        String expectedResponse =
                "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 14\r\n" +
                "\r\n" +
                "file1 contents";
        Response response = buildResponse(200, Method.GET, 14, "file1 contents");

        String actualResponse = response.toString();

        assertEquals(expectedResponse, actualResponse);
    }

    private Response buildResponse(int statusCode, String method, int contentLength, String body) {
        String[][] rawHeaders = {{"Content-Length", Integer.toString(contentLength)}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);
        byte[] rawBody = body.getBytes();
        Response response = new Response(statusCode, method, headers, rawBody);

        return response;
    }

}
