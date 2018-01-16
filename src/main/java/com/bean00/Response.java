package com.bean00;

import java.util.HashMap;

public class Response {
    private static final String PROTOCOL_VERSION = "HTTP/1.1";
    private int statusCode;
    private String message;
    private static HashMap<Integer, String> statusMessages = buildMessages();

    public Response(int statusCode) {
        this.statusCode = statusCode;
        this.message = statusMessages.get(statusCode);
    }

    @Override
    public String toString() {
        return PROTOCOL_VERSION + " " + statusCode + " " + message + "\r";
    }

    private static HashMap<Integer, String> buildMessages() {
        HashMap<Integer, String> statusMessages = new HashMap<>();

        statusMessages.put(200, "OK");
        statusMessages.put(404, "Not Found");

        return statusMessages;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
