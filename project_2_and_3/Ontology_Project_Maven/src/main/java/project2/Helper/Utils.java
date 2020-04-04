package project2.Helper;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class Utils {
    public static String stripLeadingZeros(final String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }
    public static String encodeToURI(String s) throws UnsupportedEncodingException {
        return URI.create(URLEncoder.encode(s, "UTF-8")).toString();
    }

    public static void main(String[] args) {

    }
}
