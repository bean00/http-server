package com.bean00.server;

import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpexception.NotFoundHttpException;
import com.bean00.request.Request;
import com.bean00.response.Response;
import com.bean00.response.Status;

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
        } catch (NotFoundHttpException e) {
            response = new Response(Status.NOT_FOUND);
        } catch (Throwable t) {
            response = new Response(Status.INTERNAL_SERVER_ERROR);
        }

        writer.writeResponse(response);
    }

}
