package com.bean00;

public class RequestInterpreter {

    public int chooseStatusCode(String requestURL) {
        int statusCode;

        if (requestURL.equals("/")) {
            statusCode = Status.OK;
        } else {
            statusCode = Status.NOT_FOUND;
        }

        return statusCode;
    }

}
