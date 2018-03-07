package com.bean00.server;

import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import com.bean00.httpmessages.Status;

import java.io.IOException;

public class Server {

    public void run(RequestParser parser, RequestProcessor processor,
                    ResponseWriter writer) throws IOException {
        Response response;
        String errorMessage = "";

        try {
            Request request = parser.parseRequest();
            response = processor.processRequest(request);
        } catch (BadRequestHttpException e) {
            response = new Response(Status.BAD_REQUEST);
            errorMessage = e.getMessage();
        } catch (Throwable t) {
            response = new Response(Status.INTERNAL_SERVER_ERROR);
        }

        writer.writeResponse(response);

        System.out.print(errorMessage);
    }

}
