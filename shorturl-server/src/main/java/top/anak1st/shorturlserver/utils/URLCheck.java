package top.anak1st.shorturlserver.utils;

import java.util.regex.Pattern;

public class URLCheck {
    private static String REGEX = "^(http|https)://.*";

    public static boolean check(String url) {
        return Pattern.matches(REGEX, url);
    }
}
