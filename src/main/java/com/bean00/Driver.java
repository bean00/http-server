package com.bean00;

import com.bean00.controllers.CobSpecController;
import com.bean00.datastore.DataStore;
import com.bean00.datastore.FileSystemDataStore;
import com.bean00.httpmessages.Method;

import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {
        Application app = new Application();
        ServerOptions options;

        try {
            options = parseArguments(args);
        } catch (IllegalArgumentException e) {
            printErrorMessage();
            return;
        }

        int portNumber = options.getPort();
        String pathToRoot = options.getDirectory();

        printServerInfo(portNumber, pathToRoot);

        app.setup(portNumber);

        DataStore dataStore = new FileSystemDataStore(pathToRoot);
        CobSpecController cobSpecController = new CobSpecController(dataStore);

        app.addRoute(Method.GET, "*", cobSpecController::handleGetAndHeadRequests);
        app.addRoute(Method.HEAD, "*", cobSpecController::handleGetAndHeadRequests);
        app.addRoute(Method.PUT, "*", cobSpecController::handlePutRequest);
        app.addRoute(Method.POST, "/cat-form", cobSpecController::handleCatFormPost);
        app.addRoute(Method.POST, "/form", cobSpecController::handleFormPost);
        app.addRoute(Method.DELETE, "*", cobSpecController::handleDeleteRequest);

        app.run();
    }

    private static ServerOptions parseArguments(String[] args) {
        DataStore dataStore = new FileSystemDataStore("");
        ArgParser argParser = new ArgParser(dataStore);
        ServerOptions options = argParser.buildServerOptions(args);

        return options;
    }

    private static void printServerInfo(int portNumber, String pathToRoot) {
        System.out.println("Server started");
        System.out.println("- Port: " + portNumber);
        System.out.println("- Directory: " + pathToRoot);
    }

    private static void printErrorMessage() {
        System.out.print(
                "[ERROR] Invalid arguments passed in.\n" +
                "\n" +
                "Proper usage:\n" +
                "> java -jar [SERVER JAR FILE] [-d DIRECTORY] [-p PORT]\n" +
                "\n" +
                "Example:\n" +
                "> java -jar target/http-server.jar -d public -p 5000\n"
        );
    }

}
