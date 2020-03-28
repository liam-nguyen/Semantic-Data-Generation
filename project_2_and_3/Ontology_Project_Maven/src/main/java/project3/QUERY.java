package project3;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class QUERY {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URI.create(URLEncoder.encode("Liam is cool", "UTF-8")));
    }
}
