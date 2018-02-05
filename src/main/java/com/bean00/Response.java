package com.bean00;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final String PROTOCOL_VERSION = "HTTP/1.1";

    private int statusCode;
    private byte[] body = new byte[0];
    private int contentLength;
    private HashMap<String, Integer> headers = new HashMap<>();
    private boolean displayBody = true;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response(int statusCode, String requestMethod, byte[] body) {
        this(statusCode);
        this.body = body;
        this.contentLength = body.length;
        addAppropriateHeaders();
        this.displayBody = !requestMethod.equals(Method.HEAD);
    }

    @Override
    public String toString() {
        String statusMessage = Status.getMessage(statusCode);

        String response = buildResponse(statusMessage);

        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public int getHeader(String header) {
        return headers.get(header);
    }

    private void addAppropriateHeaders() {
        addContentLengthHeader();
    }

    private void addContentLengthHeader() {
        headers.put("Content-Length", contentLength);
    }

    private String buildResponse(String statusMessage) {
        String response = getStatusLine(statusMessage);

        response += getHeadersAsAString();

        response += getBlankLine();

        if (displayBody) {
            response += getBodyAsAString();
        }

        return response;
    }

    private String getStatusLine(String statusMessage) {
        String statusLine = PROTOCOL_VERSION + " " + statusCode + " " + statusMessage + "\r\n";

        return statusLine;
    }

    private String getHeadersAsAString() {
        String headersAsAString = "";

        if (!headers.isEmpty()) {
            for (Map.Entry<String, Integer> header : headers.entrySet()) {
                String headerString = header.getKey() + ": " + header.getValue() + "\r\n";
                headersAsAString += headerString;
            }
        }

        return headersAsAString;
    }

    private String getBlankLine() {
        String blankLine = "\r\n";

        return blankLine;
    }

    private String getBodyAsAString() {
        String bodyAsAString = new String(body);

        return bodyAsAString;
    }

}
