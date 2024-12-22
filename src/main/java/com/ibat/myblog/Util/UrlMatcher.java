package com.ibat.myblog.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlMatcher {

    private static final Pattern URL_PATTERN = Pattern.compile(
        "(https?://[\\w-]+(\\.[\\w-]+)+(\\S*)?)");

    private static final String[] MEDIA_SITES = {
        "music.qq.com",
        "y.qq.com",
        "music.163.com",
        "v.qq.com",
        "iqiyi.com"
    };

    public static List<String> extractUrls(String content) {
        List<String> urls = new ArrayList<>();
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }

    public static boolean isMediaUrl(String url) {
        for (String site : MEDIA_SITES) {
            if (url.contains(site)) {
                return true;
            }
        }
        return false;
    }
}