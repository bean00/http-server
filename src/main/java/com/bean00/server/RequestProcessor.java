package com.bean00.server;

import com.bean00.datastore.DataStore;
import com.bean00.httpexception.NotFoundHttpException;
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

        if (!dataStore.resourceExists(requestURL)) {
            throw new NotFoundHttpException();
        }

        byte[] body = dataStore.getResource(requestURL);
        HashMap<String, String> headers = buildHeaders(body, requestURL);
        Response response = new Response(Status.OK, requestMethod, headers, body);

        return response;
    }

    private HashMap<String, String> buildHeaders(byte[] body, String requestURL) throws IOException {
        String contentLength = Integer.toString(body.length);
        String contentType = dataStore.getMediaType(requestURL);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Length", contentLength);
        headers.put("Content-Type", contentType);

        return headers;
    }

}
