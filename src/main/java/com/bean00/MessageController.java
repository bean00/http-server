package com.bean00;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessageController {

    public Response processRequest(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        Path path = getFullPath(requestURL);

        boolean rootDirectoryWasRequested = requestURL.equals("/");
        boolean fileCanBeFound = checkIfFileCanBeFound(path);

        byte[] body = new byte[0];
        if (fileCanBeFound) {
            body = getFileContents(path);
        }

        Response response;
        if (rootDirectoryWasRequested) {
            response = new Response(Status.OK);
        } else if (!fileCanBeFound) {
            response = new Response(Status.NOT_FOUND);
        } else {
            response = new Response(Status.OK, body);
        }

        return response;
    }

    public void writeResponse(Response response, Writer out) throws IOException {
        String responseString = response.toString();

        out.write(responseString);
        out.flush();
    }

    private Path getFullPath(String requestURL) {
        String pathToPublic = "/Users/jonchin/8th-Light/projects/java-server/cob_spec/public";

        Path path = Paths.get(pathToPublic, requestURL);

        return path;
    }

    private boolean checkIfFileCanBeFound(Path path) {
        boolean fileExists = Files.exists(path);
        boolean pathPointsToDirectory = Files.isDirectory(path);

        boolean fileCanBeFound = fileExists && !pathPointsToDirectory;

        return fileCanBeFound;
    }

    private byte[] getFileContents(Path path) throws IOException {
        byte[] body = Files.readAllBytes(path);

        return body;
    }

}
