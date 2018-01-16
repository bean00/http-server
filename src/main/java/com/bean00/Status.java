package com.bean00;

import java.util.HashMap;

public class Status {
    public static final int OK = 200;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

    private static HashMap<Integer, String> statusMessages = buildMessages();

    public static String getMessage(int statusCode) {
        String statusMessage = statusMessages.get(statusCode);

        return statusMessage;
    }

    private static HashMap<Integer, String> buildMessages() {
        HashMap<Integer, String> statusMessages = new HashMap<>();

        statusMessages.put(OK, "OK");
        statusMessages.put(NOT_FOUND, "Not Found");
        statusMessages.put(INTERNAL_SERVER_ERROR, "Internal Server Error");

        return statusMessages;
    }

}
