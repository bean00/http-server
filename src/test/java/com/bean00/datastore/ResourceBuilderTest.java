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
    public void buildHtmlBody_returnsHTML_thatHasTheSortedList_ofFiles() {
        String expectedHTML =
                "<li>directory</li>\n" +
                "<li>file1</li>\n" +
                "<li>file2</li>\n";
        ResourceBuilder builder = new ResourceBuilder();
        File[] files = buildFilesArray();

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

    private File[] buildFilesArray() {
        File file1 = new File("/file1");
        File file2 = new File("/file2");
        File directory = new File("/directory");
        directory.mkdir();
        File[] files = {file1, file2, directory};

        return files;
    }

}
