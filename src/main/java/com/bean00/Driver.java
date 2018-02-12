package com.bean00;

import com.bean00.datastore.DataStore;
import com.bean00.datastore.FileSystemDataStore;
import com.bean00.server.RequestParser;
import com.bean00.server.RequestProcessor;
import com.bean00.server.ResponseWriter;
import com.bean00.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver {

    public static void main(String[] args) throws IOException {
        String pathToRoot;

        try {
            ArgParser argParser = new ArgParser();
            ServerOptions options = argParser.buildServerOptions(args);
            pathToRoot = options.getDirectory();
        } catch (IllegalArgumentException e) {
            printErrorMessage();
            return;
        }

        int portNumber = 5000;
        System.out.println("Server started");
        System.out.println("- Port: " + portNumber);
        System.out.println("- Directory: " + pathToRoot);

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

    private static void printErrorMessage() {
        System.out.print(
                "[ERROR] Invalid arguments passed in.\n" +
                "\n" +
                "Proper usage:\n" +
                "> java -jar [SERVER JAR FILE] [-d DIRECTORY]\n" +
                "\n" +
                "Example:\n" +
                "> java -jar target/http-server.jar -d public\n"
        );
    }

}
