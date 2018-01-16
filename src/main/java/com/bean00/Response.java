package com.bean00;

public class Response {
    private static final String PROTOCOL_VERSION = "HTTP/1.1";
    private int statusCode;
    private String message;

    public Response(int statusCode) {
        this.statusCode = statusCode;
        this.message = Status.getMessage(statusCode);
    }

    @Override
    public String toString() {
        return PROTOCOL_VERSION + " " + statusCode + " " + message + "\r\n";
    }

    public int getStatusCode() {
        return statusCode;
    }

}
