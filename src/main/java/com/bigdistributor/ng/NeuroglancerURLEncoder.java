package com.bigdistributor.ng;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class NeuroglancerURLEncoder {
    private static final String baseUrl = "https://neuroglancer-demo.appspot.com/#!";

    public static void main(String[] args) throws IOException, URISyntaxException {
        URL resource = NeuroglancerURLEncoder.class.getClassLoader().getResource("NeuroglancerExample.json");
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        }

        File jsonfile = new File(resource.toURI());

        System.out.println("Json path: " + jsonfile.getAbsolutePath());

        String jsonContent = readJsonFile(jsonfile.getAbsolutePath());

        System.out.println(jsonContent);
        System.out.println();
        String encodedJson = URLEncoder.encode(jsonContent, "UTF-8");
        System.out.println(encodedJson);
        String fullUrl = baseUrl+encodedJson;
        openInBrowser(fullUrl);
    }


    private static String readJsonFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s.replace(" ","")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    private static void openInBrowser(String fullUrl) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(fullUrl));
        }
    }
}
