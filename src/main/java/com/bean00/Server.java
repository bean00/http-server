package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Server {
    private MessageController messageController;

    public Server(BufferedReader in, PrintWriter out) {
        messageController = new MessageController(in, out);
    }

    public void handleGET() throws IOException {
        Request request = messageController.readRequest();

        Response response = messageController.interpretRequest(request);

        messageController.writeResponse(response);
    }

}
