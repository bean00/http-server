package com.bean00;

import java.util.List;

public class Request {
    private String requestMethod;
    private String requestURL;

    public Request(String requestMethod, String requestURL, List<String> headers) {
        this.requestMethod = requestMethod;
        this.requestURL = requestURL;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }

}
