package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestParser {
    private BufferedReader in;
    private static final int EXPECTED_NUMBER_OF_REQUEST_WORDS = 3;
    private static final String EXPECTED_HTTP_VERSION = "HTTP/1.1";

    public RequestParser(BufferedReader in) {
        this.in = in;
    }

    public Request parseRequest() throws IOException {
        String requestLine = in.readLine();
        if (requestLine == null) { throw new BadRequestHttpException(); }

        requestLine = requestLine.trim();
        String[] requestWords = requestLine.split("\\s+");
        if (requestWords.length != EXPECTED_NUMBER_OF_REQUEST_WORDS) { throw new BadRequestHttpException(); }

        String requestMethod = requestWords[0];
        String requestURL = requestWords[1];

        String httpVersion = requestWords[2];
        if (!httpVersion.equals(EXPECTED_HTTP_VERSION)) { throw new BadRequestHttpException(); }

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
