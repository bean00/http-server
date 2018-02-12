package com.bean00.server;

import com.bean00.datastore.DataStore;
import com.bean00.request.Method;
import com.bean00.request.Request;
import com.bean00.response.Response;
import com.bean00.response.Status;

import java.io.IOException;
import java.util.HashMap;

public class RequestProcessor {
    private DataStore dataStore;

    public RequestProcessor(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response processRequest(Request request) throws IOException {
        String requestMethod = request.getRequestMethod();
        String requestURL = request.getRequestURL();

        Response response;
        if (requestURL.equals("/")) {
            response = new Response(Status.OK);
        } else if (!dataStore.dataCanBeFound(requestURL)) {
            response = new Response(Status.NOT_FOUND);
        } else if (requestMethod.equals("HEAD")) {
            byte[] body = dataStore.getData(requestURL);
            HashMap<String, String> headers = buildHeaders(body, requestURL);
            response = new Response(Status.OK, Method.HEAD, headers, body);
        } else {
            byte[] body = dataStore.getData(requestURL);
            HashMap<String, String> headers = buildHeaders(body, requestURL);
            response = new Response(Status.OK, Method.GET, headers, body);
        }

        return response;
    }

    private HashMap<String, String> buildHeaders(byte[] body, String requestURL) throws IOException {
        HashMap<String, String> headers = new HashMap<>();

        int contentLength = body.length;
        String contentLengthAsString = Integer.toString(contentLength);
        headers.put("Content-Length", contentLengthAsString);

        String contentType = dataStore.getMediaType(requestURL);
        headers.put("Content-Type", contentType);

        return headers;
    }

}
