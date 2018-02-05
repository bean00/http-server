package com.bean00;

public class ServerErrorRequestProcessor extends RequestProcessor {

    @Override
    public Response processRequest(Request request) {
        throw new RuntimeException();
    }

}
