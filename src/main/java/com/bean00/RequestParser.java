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
        String requestLine = in.readLine();
        String[] requestWords = requestLine.split("\\s");

        String requestMethod = requestWords[0];
        String requestURL = requestWords[1];
        List<String> headers = parseHeaders();

        Request request = new Request(requestMethod, requestURL, headers);

        return request;
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
