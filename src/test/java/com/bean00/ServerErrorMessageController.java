package com.bean00;

public class ServerErrorMessageController extends MessageController {

    @Override
    public Response processRequest(Request request) {
        throw new RuntimeException();
    }

}
