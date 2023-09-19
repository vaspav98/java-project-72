package hexlet.code;

import java.net.MalformedURLException;
import java.net.URL;

public class Check {

    public static void main(String[] arg) throws MalformedURLException {
        URL url = new URL("https://some-domain.org/example/path");
        System.out.println("Protocol: " + url.getProtocol());
        System.out.println("Host: " + url.getHost());
        System.out.println("Port: " + url.getPort());
        System.out.println("Path: " + url.getPath());
        System.out.println("Query: " + url.getQuery());
        System.out.println("File: " + url.getFile());
        System.out.println("Ref: " + url.getRef());
    }
/*    ${for (var urlCheck : page.getUrlChecks()) {
        if (urlCheck.getUrlId() == url.getId()) {
            return urlCheck.getCreatedAt().toString()}
    }*/
}
