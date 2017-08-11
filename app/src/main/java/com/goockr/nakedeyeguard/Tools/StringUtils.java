package com.goockr.nakedeyeguard.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bryce on 2016/4/1.
 */
public class StringUtils {

    public static boolean isEmpty(String value){
        return value == null || value.trim().length() == 0;
    }

    public static String isEmptyString(String value) {
        return isEmpty(value) ? "" : value;
    }

    public static String isEmptyString1(String value) {
        return isEmpty(value) ? "------" : value;
    }

    // 判别是否包含Emoji表情
    public static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //正则判断是否有emoji表情
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
}
