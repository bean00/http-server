package com.bean00;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final String PROTOCOL_VERSION = "HTTP/1.1";

    private int statusCode;
    private HashMap<String, String> headers = new HashMap<>();
    private byte[] body = new byte[0];
    private boolean displayBody = true;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response(int statusCode, String requestMethod, HashMap<String, String> headers, byte[] body) {
        this(statusCode);
        this.headers = headers;
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

    public String getResponseAsString() throws IOException {
        byte[] responseAsByteArray = getResponseAsByteArray();

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
        return headers.get(header);
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
        String headersAsAString = "";

        for (Map.Entry<String, String> header : headers.entrySet()) {
            String headerString = header.getKey() + ": " + header.getValue() + "\r\n";
            headersAsAString += headerString;
        }

        return headersAsAString.getBytes();
    }

    private byte[] getBlankLine() {
        String blankLine = "\r\n";

        return blankLine.getBytes();
    }

}
