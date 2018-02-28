package com.bean00.server;

import com.bean00.httpmessages.HttpHeaders;
import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.Request;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    private static final int EXPECTED_NUMBER_OF_REQUEST_WORDS = 3;
    private static final String EXPECTED_HTTP_VERSION = "HTTP/1.1";

    private BufferedReader in;

    public RequestParser(BufferedReader in) {
        this.in = in;
    }

    public Request parseRequest() throws IOException {
        String requestLine = in.readLine();
        if (requestLine == null) {
            throw new BadRequestHttpException();
        }

        requestLine = requestLine.trim();
        String[] requestWords = requestLine.split("\\s+");
        if (requestWords.length != EXPECTED_NUMBER_OF_REQUEST_WORDS) {
            throw new BadRequestHttpException();
        }

        String requestMethod = requestWords[0];
        String requestURL = requestWords[1];
        String httpVersion = requestWords[2];

        HttpHeaders headers = parseHeaders();

        int contentLength = 0;
        if (headers.headerExists("Content-Length")) {
            contentLength = Integer.parseInt(headers.getHeader("Content-Length"));
        }

        String body = parseBody(contentLength);

        if (!httpVersion.equals(EXPECTED_HTTP_VERSION)) {
            throw new BadRequestHttpException();
        }

        Request request = new Request(requestMethod, requestURL, headers, body);

        return request;
    }

    private HttpHeaders parseHeaders() throws IOException {
        String header;
        HttpHeaders headers = new HttpHeaders();

        while ((header = in.readLine()) != null && !isLineBlank(header)) {
            String[] headerParts = header.split(":", 2);

            String fieldName = headerParts[0];
            String fieldValue = headerParts[1].trim();

            if (hasLeadingOrTrailingWhitespace(fieldName)) {
                throw new BadRequestHttpException();
            }

            headers.setHeader(fieldName, fieldValue);
        }

        return headers;
    }

    private boolean isLineBlank(String line) {
        return line.isEmpty();
    }

    private boolean hasLeadingOrTrailingWhitespace(String string) {
        String trimmedString = string.trim();

        return !string.equals(trimmedString);
    }

    private String parseBody(int contentLength) throws IOException {
        char[] bodyBuffer = new char[contentLength];
        in.read(bodyBuffer, 0, contentLength);
        String body = new String(bodyBuffer);

        return body;
    }

}
