package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

public class Server {

    public void run(MessageController messageController,
                    BufferedReader in, Writer out) throws IOException {
        try {
            RequestParser parser = new RequestParser(in);

            Request request = parser.parseRequest();

            Response response = messageController.processRequest(request);

            messageController.writeResponse(response, out);
        } catch (Throwable t) {
            Response response = new Response(Status.INTERNAL_SERVER_ERROR);

            messageController.writeResponse(response, out);
        }
    }

}
