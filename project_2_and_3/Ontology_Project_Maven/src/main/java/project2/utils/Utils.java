package project2.utils;

public class Utils {
    public static String stripLeadingZeros(final String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }
}
