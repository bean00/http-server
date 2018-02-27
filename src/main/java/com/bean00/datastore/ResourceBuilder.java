package com.bean00.datastore;

import java.io.File;
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

    public byte[] buildHtmlBody(File[] files) {
        String htmlAsString = htmlBefore + getHtmlForListOfContents(files) + htmlAfter;
        byte[] body = htmlAsString.getBytes();

        return body;
    }

    private String getHtmlForListOfContents(File[] files) {
        String list = "";
        Arrays.sort(files);

        for (File file : files) {
            list += addListTags(file.getName());
        }

        return list;
    }

    private String addListTags(String field) {
        return "<li>" + field + "</li>\n";
    }

}
