package com.bean00.datastore;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class ResourceBuilder {
    private String htmlBefore =
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title>Server</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<ul>\n";
    private String htmlAfter =
            "</ul>\n" +
            "</body>\n" +
            "</html>\n";

    public byte[] buildHtmlBody(Path pathToRoot, String requestURL) {
        File[] files = getFiles(pathToRoot);
        String htmlAsString = htmlBefore + getHtmlForContents(files, requestURL) + htmlAfter;
        byte[] body = htmlAsString.getBytes();

        return body;
    }

    private File[] getFiles(Path path) {
        String absoluteUrl = path.toString();
        File directory = new File(absoluteUrl);

        return directory.listFiles();
    }

    private String getHtmlForContents(File[] files, String requestURL) {
        String list = "";
        Arrays.sort(files);

        for (File file : files) {
            list += buildElement(file, requestURL);
        }

        return list;
    }

    private String buildElement(File file, String requestURL) {
        String fileName = file.getName();
        String fileURL = buildFileURL(requestURL, fileName);

        String element = "<li><a href=\"" + fileURL + "\">" + fileName + "</a></li>\n";

        return element;
    }

    private String buildFileURL(String requestURL, String fileName) {
        String fileURL = "/" + fileName;

        if (!requestURL.equals("/")) {
            fileURL = requestURL + fileURL;
        }

        return fileURL;
    }

}
