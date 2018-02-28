package com.bean00.datastore;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceBuilderTest {
    private ResourceBuilder builder = new ResourceBuilder();

    @Test
    public void buildHtmlBody_returnsHTML_thatHasATitle() {
        String expectedTitle = "<title>Server</title>";
        Path path = getPath(FileSystemDataStoreTest.PATH_TO_TEST_FILES);

        byte[] body = builder.buildHtmlBody(path, "/");
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedTitle));
    }

    @Test
    public void buildHtmlBody_returnsHTML_thatHasEverythingNeeded_beforeTheList() {
        String expectedHTML =
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Server</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<ul>\n";
        Path path = getPath(FileSystemDataStoreTest.PATH_TO_TEST_FILES);

        byte[] body = builder.buildHtmlBody(path, "/");
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void buildHtmlBody_returnsHTML_thatHasAListOfFiles_sorted() {
        String expectedHTML =
                "<li><a href=\"/directory/file1\">file1</a></li>\n" +
                "<li><a href=\"/directory/file2\">file2</a></li>\n" +
                "<li><a href=\"/directory/file3\">file3</a></li>\n";
        Path path = getPath(FileSystemDataStoreTest.PATH_TO_TEST_FILES + "/directory");

        byte[] body = builder.buildHtmlBody(path, "/directory");
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void buildHtmlBody_returnsHTML_forTheRootDirectory() {
        String expectedHTML =
                "<li><a href=\"/directory\">directory</a></li>\n";
        Path path = getPath(FileSystemDataStoreTest.PATH_TO_TEST_FILES);

        byte[] body = builder.buildHtmlBody(path, "/");
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void buildHtmlBody_returnsHTML_thatHasEverythingNeeded_afterTheList() {
        String expectedHTML =
                "</ul>\n" +
                "</body>\n" +
                "</html>\n";
        Path path = getPath(FileSystemDataStoreTest.PATH_TO_TEST_FILES);

        byte[] body = builder.buildHtmlBody(path, "/");
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    private Path getPath(String pathString) {
        return Paths.get(pathString);
    }

}
