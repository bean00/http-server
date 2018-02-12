package com.bean00;

import java.io.IOException;

public class Server {

    public void run(RequestParser parser, RequestProcessor processor,
                    ResponseWriter writer) throws IOException {
        Response response;

        try {
            Request request = parser.parseRequest();

            response = processor.processRequest(request);
        } catch (BadRequestHttpException e) {
            response = new Response(Status.BAD_REQUEST);
        } catch (Throwable t) {
            response = new Response(Status.INTERNAL_SERVER_ERROR);
        }

        writer.writeResponse(response);
    }

}
