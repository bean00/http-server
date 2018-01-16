package com.bean00;

import java.util.List;

public class Request {
    private String requestLine;
    private String target;

    public Request(String requestLine, List<String> headers) {
        RequestParser parser = new RequestParser();
        this.requestLine = requestLine;
        this.target = parser.getRequestTarget(requestLine);
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getTarget() {
        return target;
    }

}
