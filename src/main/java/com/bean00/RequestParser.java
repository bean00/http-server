package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestParser {
    private BufferedReader in;

    public RequestParser(BufferedReader in) {
        this.in = in;
    }

    public Request parseRequest() throws IOException {
        String requestURL = parseRequestLine();
        List<String> headers = parseHeaders();

        Request request = new Request(requestURL, headers);

        return request;
    }

    private String parseRequestLine() throws IOException {
        String requestLine = in.readLine();

        String requestURL = getRequestURL(requestLine);

        return requestURL;
    }

    private String getRequestURL(String requestLine) {
        String[] requestWords = requestLine.split("\\s");

        String requestTarget = requestWords[1];

        return requestTarget;
    }

    private List<String> parseHeaders() throws IOException {
        String header;
        List<String> headers = new ArrayList<>();

        while ((header = in.readLine()) != null && !isLineBlank(header)) {
            headers.add(header);
        }

        return headers;
    }

    private boolean isLineBlank(String line) {
        return line.isEmpty();
    }

}
