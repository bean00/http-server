package com.bean00.datastore;

import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemDataStore implements DataStore {
    private String pathToRoot;

    public FileSystemDataStore(String pathToRoot) {
        this.pathToRoot = pathToRoot;
    }

    public boolean directoryExists(String url) {
        Path path = getFullPath(url);
        boolean pathExists = Files.exists(path);
        boolean isDirectory = Files.isDirectory(path);

        return pathExists && isDirectory;
    }

    public boolean fileExists(String url) {
        Path path = getFullPath(url);
        boolean pathExists = Files.exists(path);
        boolean isDirectory = Files.isDirectory(path);

        return pathExists && !isDirectory;
    }

    public byte[] getData(String url) throws IOException {
        Path path = getFullPath(url);
        return Files.readAllBytes(path);
    }

    public String getMediaType(String url) throws IOException {
        Path path = getFullPath(url);
        Tika tika = new Tika();
        String contentType = tika.detect(path);

        return contentType;
    }

    private Path getFullPath(String requestURL) {
        Path path = Paths.get(pathToRoot, requestURL);

        return path;
    }

}
