package com.bean00.httpmessages;

public class Request {
    private String requestMethod;
    private String requestURL;
    private HttpHeaders headers = new HttpHeaders();
    private String body = "";

    public Request(String requestMethod, String requestURL) {
        this.requestMethod = requestMethod;
        this.requestURL = requestURL;
    }

    public Request(String requestMethod, String requestURL, HttpHeaders headers) {
        this(requestMethod, requestURL);
        this.headers = headers;
    }

    public Request(String requestMethod, String requestURL, HttpHeaders headers, String body) {
        this(requestMethod, requestURL, headers);
        this.body = body;
    }

    @Override
    public String toString() {
        String request = String.format("%s %s HTTP/1.1\r\n", requestMethod, requestURL);

        request += headers.toString() + "\r\n" + body;

        return request;
    }

    public String getHeader(String fieldName) {
        return headers.getHeader(fieldName);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getBody() {
        return body;
    }

    public byte[] getBodyAsBytes() {
        return body.getBytes();
    }

}
