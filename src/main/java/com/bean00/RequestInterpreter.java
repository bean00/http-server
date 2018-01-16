package com.bean00;

public class RequestInterpreter {

    public int chooseStatusCode(String target) {
        int statusCode;

        if (target.equals("/")) {
            statusCode = 200;
        } else {
            statusCode = 404;
        }

        return statusCode;
    }

}
