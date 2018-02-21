package com.bean00.datastore;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemDataStoreTest {
    private static final String PATH_TO_ROOT = "src/test/resources/test-files";
    private FileSystemDataStore dataStore = new FileSystemDataStore(PATH_TO_ROOT);

    @Test
    public void resourceExists_returnsFalse_whenTheResourceDoesNotExist() {
        boolean resourceExists = dataStore.resourceExists("/xx");

        assertFalse(resourceExists);
    }

    @Test
    public void resourceExists_returnsTrue_whenTheResourceExists() {
        boolean resourceExists = dataStore.resourceExists("/directory");

        assertTrue(resourceExists);
    }

    @Test
    public void getResource_returnsTheCorrectData_forAFileWithContent() throws IOException {
        byte[] expectedData = "file1 contents".getBytes();

        byte[] data = dataStore.getResource("/file1");

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void getResource_returnsNoData_forAnEmptyFile() throws IOException {
        byte[] expectedData = new byte[0];

        byte[] data = dataStore.getResource("/no-content");

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void getResource_returnsTheCorrectData_forADirectoryListing() throws IOException {
        String expectedHTML =
                "<li>file1</li>\n" +
                "<li>file2</li>\n" +
                "<li>file3</li>\n";

        byte[] data = dataStore.getResource("/directory");
        String dataAsString = new String(data);

        assertTrue(dataAsString.contains(expectedHTML));
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forATextFile() throws IOException {
        String expectedMediaType = "text/plain";

        String mediaType = dataStore.getMediaType("/text-file.txt");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forAnImageFile() throws IOException {
        String expectedMediaType = "image/jpeg";

        String mediaType = dataStore.getMediaType("/image.jpeg");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forAnUnknownFile() throws IOException {
        String expectedMediaType = "application/octet-stream";

        String mediaType = dataStore.getMediaType("/executable");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheMediaType_forAFileWithoutAnExtension() throws IOException {
        String expectedMediaType = "text/plain";

        String mediaType = dataStore.getMediaType("/file1");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheHTMLMediaType_forADirectory() throws IOException {
        String expectedMediaType = "text/html; charset=utf-8";

        String mediaType = dataStore.getMediaType("/directory");

        assertEquals(expectedMediaType, mediaType);
    }

}
