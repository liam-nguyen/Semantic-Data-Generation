package project2.Util;

import com.google.common.net.UrlEscapers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class URI_Helper {
    public static String stripLeadingZeros(final String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }
    public static String escapeURI(String s) {
        return UrlEscapers.urlFragmentEscaper().escape(s);
    }
}
