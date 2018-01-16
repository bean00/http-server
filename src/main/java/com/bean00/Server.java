package com.bean00;

public class Server {

    public void run(MessageController messageController) {
        try {
            Request request = messageController.getRequest();

            Response response = messageController.interpretRequest(request);

            messageController.writeResponse(response);
        } catch (Throwable t) {
            Response response = new Response(Status.INTERNAL_SERVER_ERROR);

            messageController.writeResponse(response);
        }
    }

}
