package com.bean00.datastore;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemDataStore implements DataStore {
    private String pathToRoot;

    public FileSystemDataStore(String pathToRoot) {
        this.pathToRoot = pathToRoot;
    }

    public boolean resourceExists(String url) {
        Path path = getFullPath(url);

        return Files.exists(path);
    }

    public byte[] getResource(String url) throws IOException {
        Path path = getFullPath(url);
        byte[] resource;

        if (Files.isRegularFile(path)) {
            resource = Files.readAllBytes(path);
        } else {
            resource = getDirectoryListing(url);
        }

        return resource;
    }

    public String getMediaType(String url) throws IOException {
        Path path = getFullPath(url);
        String contentType;

        if (!Files.isDirectory(path)) {
            Tika tika = new Tika();
            contentType = tika.detect(path);
        } else {
            String HTMLContentType = "text/html; charset=utf-8";
            contentType = HTMLContentType;
        }

        return contentType;
    }

    private Path getFullPath(String requestURL) {
        Path path = Paths.get(pathToRoot, requestURL);

        return path;
    }

    private byte[] getDirectoryListing(String url) {
        File[] files = getFiles(pathToRoot + url);
        ResourceBuilder resourceBuilder = new ResourceBuilder();
        byte[] directoryListing = resourceBuilder.buildHtmlBody(files);

        return directoryListing;
    }

    private File[] getFiles(String absoluteUrl) {
        File directory = new File(absoluteUrl);

        return directory.listFiles();
    }

}
