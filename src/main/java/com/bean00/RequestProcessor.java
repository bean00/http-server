package com.bean00;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestProcessor {

    public Response processRequest(Request request) throws IOException {
        String requestMethod = request.getRequestMethod();
        String requestURL = request.getRequestURL();
        Path path = getFullPath(requestURL);

        Response response;
        if (requestURL.equals("/")) {
            response = new Response(Status.OK);
        } else if (!fileCanBeFound(path)) {
            response = new Response(Status.NOT_FOUND);
        } else if (requestMethod.equals("HEAD")) {
            byte[] body = getFileContents(path);
            response = new Response(Status.OK, Method.HEAD, body);
        } else {
            byte[] body = getFileContents(path);
            response = new Response(Status.OK, Method.GET, body);
        }

        return response;
    }

    private Path getFullPath(String requestURL) {
        String pathToPublic = "/Users/jonchin/8th-Light/projects/java-server/cob_spec/public";

        Path path = Paths.get(pathToPublic, requestURL);

        return path;
    }

    private boolean fileCanBeFound(Path path) {
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
