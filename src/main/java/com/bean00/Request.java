package com.bean00;

import java.util.List;

public class Request {
    private String requestURL;

    public Request(String requestURL, List<String> headers) {
        this.requestURL = requestURL;
    }

    public String getRequestURL() {
        return requestURL;
    }

}
