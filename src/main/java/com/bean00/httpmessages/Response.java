package com.bean00.httpmessages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Response {
    private static final String PROTOCOL_VERSION = "HTTP/1.1";

    private int statusCode;
    private HttpHeaders headers = new HttpHeaders();
    private byte[] body = new byte[0];
    private boolean displayBody = true;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response(int statusCode, HttpHeaders headers) {
        this(statusCode);
        this.headers = headers;
    }

    public Response(int statusCode, String requestMethod, HttpHeaders headers, byte[] body) {
        this(statusCode, headers);
        this.body = body;
        this.displayBody = !requestMethod.equals(Method.HEAD);
    }

    public byte[] getResponseAsByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = writeOutResponseBeforeBody();

        if (displayBody) {
            byteArrayOutputStream.write(body);
        }

        byte[] response = byteArrayOutputStream.toByteArray();

        return response;
    }

    @Override
    public String toString() {
        byte[] responseAsByteArray = new byte[0];
        try {
            responseAsByteArray = getResponseAsByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = new String(responseAsByteArray);

        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getHeader(String header) {
        return headers.getHeader(header);
    }

    private ByteArrayOutputStream writeOutResponseBeforeBody() throws IOException {
        byte[] statusLine = getStatusLine();
        byte[] headers = getHeaders();
        byte[] blankLine = getBlankLine();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(statusLine);
        byteArrayOutputStream.write(headers);
        byteArrayOutputStream.write(blankLine);

        return byteArrayOutputStream;
    }

    private byte[] getStatusLine() {
        String statusMessage = Status.getMessage(statusCode);

        String statusLine = PROTOCOL_VERSION + " " + statusCode + " " + statusMessage + "\r\n";

        return statusLine.getBytes();
    }

    private byte[] getHeaders() {
        String headersAsAString = headers.toString();

        return headersAsAString.getBytes();
    }

    private byte[] getBlankLine() {
        String blankLine = "\r\n";

        return blankLine.getBytes();
    }

}
