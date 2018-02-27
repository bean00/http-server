package com.bean00.datastore;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceBuilderTest {
    private File[] emptyFileArray = new File[0];

    @Test
    public void buildHtmlBody_returnsHTML_thatHasATitle() {
        String expectedTitle = "<title>Server</title>";
        ResourceBuilder builder = new ResourceBuilder();

        byte[] body = builder.buildHtmlBody(emptyFileArray);
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
        ResourceBuilder builder = new ResourceBuilder();

        byte[] body = builder.buildHtmlBody(emptyFileArray);
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void buildHtmlBody_returnsHTML_thatHasASortedList_ofFilesAndDirectories() {
        String expectedHTML =
                "<li><a href=\"/directory\">directory</a></li>\n" +
                "<li><a href=\"/file1\">file1</a></li>\n" +
                "<li><a href=\"/file2\">file2</a></li>\n" +
                "<li><a href=\"/file3\">file3</a></li>\n";
        File directory = new File(FileSystemDataStoreTest.PATH_TO_TEST_FILES + "/directory");
        File[] files = directory.listFiles();
        ResourceBuilder builder = new ResourceBuilder();

        byte[] body = builder.buildHtmlBody(files);
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void buildHtmlBody_returnsHTML_thatHasEverythingNeeded_afterTheList() {
        String expectedHTML =
                "</ul>\n" +
                "</body>\n" +
                "</html>\n";
        ResourceBuilder builder = new ResourceBuilder();

        byte[] body = builder.buildHtmlBody(emptyFileArray);
        String bodyAsString = new String(body);

        assertTrue(bodyAsString.contains(expectedHTML));
    }

}
