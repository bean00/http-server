package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver {

    public static void main(String[] args) throws IOException {
        int portNumber = 5000;
        String pathToRoot = "/Users/jonchin/8th-Light/projects/java-server/cob_spec/public";
        System.out.println("Server started on port " + portNumber);

        ServerSocket serverSocket = new ServerSocket(portNumber);
        Server server = new Server();
        DataStore dataStore = new FileSystemDataStore(pathToRoot);
        RequestProcessor requestProcessor = new RequestProcessor(dataStore);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream();

            RequestParser requestParser = new RequestParser(reader);
            ResponseWriter responseWriter = new ResponseWriter(outputStream);

            server.run(requestParser, requestProcessor, responseWriter);

            outputStream.close();
            reader.close();
            clientSocket.close();
        }
    }

}
