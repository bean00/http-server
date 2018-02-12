package com.bean00;

import java.util.HashMap;

public class Status {
    // 2XX Success responses
    public static final int OK = 200;

    // 4XX Client error responses
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;

    // 5XX Server error responses
    public static final int INTERNAL_SERVER_ERROR = 500;

    private static HashMap<Integer, String> statusMessages = buildMessages();

    public static String getMessage(int statusCode) {
        String statusMessage = statusMessages.get(statusCode);

        return statusMessage;
    }

    private static HashMap<Integer, String> buildMessages() {
        HashMap<Integer, String> statusMessages = new HashMap<>();

        // 2XX Success responses
        statusMessages.put(OK, "OK");

        // 4XX Client error responses
        statusMessages.put(BAD_REQUEST, "Bad Request");
        statusMessages.put(NOT_FOUND, "Not Found");

        // 5XX Server error responses
        statusMessages.put(INTERNAL_SERVER_ERROR, "Internal Server Error");

        return statusMessages;
    }

}
