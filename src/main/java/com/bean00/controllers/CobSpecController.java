package com.bean00.controllers;

import com.bean00.datastore.DataStore;
import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.HttpHeaders;
import com.bean00.httpmessages.Method;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import com.bean00.httpmessages.Status;

import java.io.IOException;

public class CobSpecController {
    private DataStore dataStore;

    public CobSpecController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response handleGetAndHeadRequests(Request request) throws IOException {
        String requestMethod = request.getRequestMethod();
        String requestURL = request.getRequestURL();

        if (!dataStore.resourceExists(requestURL)) {
            return new Response(Status.NOT_FOUND);
        }

        byte[] body = dataStore.getResource(requestURL);
        HttpHeaders headers = setContentLengthAndType(body, requestURL);

        return new Response(Status.OK, requestMethod, headers, body);
    }

    public Response handlePutRequest(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        byte[] requestBody = request.getBodyAsBytes();

        int statusCode = dataStore.resourceExists(requestURL) ? Status.OK : Status.CREATED;

        dataStore.put(requestURL, requestBody);

        return new Response(statusCode);
    }

    public Response handleCatFormPost(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        byte[] requestBody = request.getBodyAsBytes();
        String postURL = requestURL + "/data";
        int statusCode;

        if (!dataStore.resourceExists(requestURL)) {
            dataStore.createDirectory(requestURL);
            statusCode = Status.CREATED;
        } else {
            statusCode = Status.OK;
        }

        dataStore.put(postURL, requestBody);

        HttpHeaders headers = new HttpHeaders();
        String location = postURL;
        headers.setHeader("Location", location);

        return new Response(statusCode, Method.POST, headers, "".getBytes());
    }

    public Response handleFormPost(Request request) {
        return new Response(Status.OK);
    }

    public Response handleDeleteRequest(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        int statusCode;

        if (dataStore.isDirectoryWithContent(requestURL)) {
            String errorMessage = "[ERROR] Trying to delete a directory that has contents.\n";
            throw new BadRequestHttpException(errorMessage);
        }

        if (!dataStore.resourceExists(requestURL)) {
            statusCode = Status.NO_CONTENT;
        } else {
            dataStore.delete(requestURL);
            statusCode = Status.OK;
        }

        return new Response(statusCode);
    }


    private HttpHeaders setContentLengthAndType(byte[] body, String requestURL) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        String contentLength = Integer.toString(body.length);
        String contentType = dataStore.getMediaType(requestURL);

        headers.setHeader("Content-Length", contentLength);
        headers.setHeader("Content-Type", contentType);

        return headers;
    }


}
