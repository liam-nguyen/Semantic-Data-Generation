package project2.Util;

import com.google.common.net.UrlEscapers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class Misc {
    public static String stripLeadingZeros(final String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }
    public static String encodeToURI(String s) throws UnsupportedEncodingException {
        return java.net.URI.create(URLEncoder.encode(s, "UTF-8")).toString();
    }
    public static String escapeURI(String s) throws UnsupportedEncodingException {
        return UrlEscapers.urlFragmentEscaper().escape(s);
    }
}
