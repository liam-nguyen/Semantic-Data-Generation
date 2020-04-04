package project2.Util;

import com.google.common.net.UrlEscapers;

public class URI_Helper {
    public static String stripLeadingZeros(final String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }
    public static String escapeURI(String s) {
        return UrlEscapers.urlFragmentEscaper().escape(s);
    }
}
