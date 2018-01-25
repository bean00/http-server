package com.bean00;

public class RequestParser {

    public String getRequestTarget(String requestLine) {
        String[] requestWords = requestLine.split("\\s");

        String requestTarget = requestWords[1];

        return requestTarget;
    }

}
