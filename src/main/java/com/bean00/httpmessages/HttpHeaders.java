package com.bean00.httpmessages;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class HttpHeaders {
    private Map<String, String> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public HttpHeaders(String[][] rawHeaders) {
        this.headers = buildHeaders(rawHeaders);
    }

    public boolean headerExists(String rawFieldName) {
        String fieldName = capitalizeFirstLetterOfEachWord(rawFieldName);

        return headers.containsKey(fieldName);
    }

    public void setHeader(String rawFieldName, String fieldValue) {
        String fieldName = capitalizeFirstLetterOfEachWord(rawFieldName);

        headers.put(fieldName, fieldValue);
    }

    public String getHeader(String rawFieldName) {
        String fieldName = capitalizeFirstLetterOfEachWord(rawFieldName);

        String fieldValue = headers.get(fieldName);

        return fieldValue;
    }

    @Override
    public String toString() {
        String headersAsAString = "";

        for (Map.Entry<String, String> header : headers.entrySet()) {
            String headerString = header.getKey() + ": " + header.getValue() + "\r\n";
            headersAsAString += headerString;
        }

        return headersAsAString;
    }

    private Map<String,String> buildHeaders(String[][] rawHeaders) {
        Map<String, String> headers = new HashMap<>();

        for (int i = 0; i < rawHeaders.length; i++) {
            String rawFieldName = rawHeaders[i][0];
            String fieldName = capitalizeFirstLetterOfEachWord(rawFieldName);

            String fieldValue = rawHeaders[i][1];

            headers.put(fieldName, fieldValue);
        }

        return headers;
    }

    private String capitalizeFirstLetterOfEachWord(String fieldName) {
        return capitalizeFully(fieldName, '-');
    }

}
