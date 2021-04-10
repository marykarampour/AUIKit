package com.prometheussoftware.auikit.utility;

import com.google.common.hash.Hashing;

import org.apache.commons.text.StringEscapeUtils;

import java.nio.charset.StandardCharsets;

public class Hash {

    public static String computeHash (String string) {
        return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
    }

    public static String securedHash (String string, String prefix, String salt) {

        if (StringUtility.isEmpty(string)) return "";
        String hashStr = StringEscapeUtils.escapeJava(prefix);
        hashStr = new StringBuilder(hashStr).append(string).append(salt).toString();
        return computeHash(hashStr);
    }
}
