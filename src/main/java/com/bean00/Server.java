package com.bean00;

import java.io.IOException;

public class Server {

    public void run(RequestParser parser, RequestProcessor processor,
                    ResponseWriter writer) throws IOException {
        try {
            Request request = parser.parseRequest();

            Response response = processor.processRequest(request);

            writer.writeResponse(response);
        } catch (Throwable t) {
            Response response = new Response(Status.INTERNAL_SERVER_ERROR);

            writer.writeResponse(response);
        }
    }

}
