package com.bean00.datastore;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemDataStoreTest {
    private static final String PATH_TO_ROOT = "src/test/resources/data-store-test-files";
    private FileSystemDataStore dataStore = new FileSystemDataStore(PATH_TO_ROOT);

    @Test
    public void directoryExists_returnsFalse_whenThePathDoesNotExist() {
        boolean directoryExists = dataStore.directoryExists("/xx");

        assertFalse(directoryExists);
    }

    @Test
    public void directoryExists_returnsFalse_whenThePathPointsToAFile() {
        boolean directoryExists = dataStore.directoryExists("/file1");

        assertFalse(directoryExists);
    }

    @Test
    public void directoryExists_returnsTrue_whenTheDirectoryExists() {
        boolean directoryExists = dataStore.directoryExists("/directory");

        assertTrue(directoryExists);
    }

    @Test
    public void fileExists_returnsFalse_whenThePathDoesNotExist() {
        boolean fileExists = dataStore.fileExists("/xx");

        assertFalse(fileExists);
    }

    @Test
    public void fileExists_returnsTrue_whenTheFileExists() {
        boolean fileExists = dataStore.fileExists("/file1");

        assertTrue(fileExists);
    }

    @Test
    public void fileExists_returnsFalse_whenTheFileIsADirectory() {
        boolean fileExists = dataStore.fileExists("/directory");

        assertFalse(fileExists);
    }

    @Test
    public void getData_returnsTheCorrectData_forAFileWithContent() throws IOException {
        byte[] expectedData = "file1 contents".getBytes();

        byte[] data = dataStore.getData("/file1");

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void getData_returnsNoData_forAnEmptyFile() throws IOException {
        byte[] expectedData = new byte[0];

        byte[] data = dataStore.getData("/no-content");

        assertArrayEquals(expectedData, data);
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

}
