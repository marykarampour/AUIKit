package com.prometheussoftware.auikit.utility;

import android.graphics.Paint;
import android.graphics.Rect;

import com.google.common.base.CaseFormat;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.model.Range;
import com.prometheussoftware.auikit.model.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {
    
    public static String format (String string, Text.TEXT_FORMAT form) {

        switch (form) {
            case CamelCase: return underScoreToCamelCase(string, false);
            case UnderScore: return camelCaseToUnderScore(string, false, false);
            case Capitalized: return string;
            case UpperCaseAll: return string;
            case CapitalizedCamelCase: return string;
            case UnderScoreIgnoreDigits: return camelCaseToUnderScore(string, true, false);
            case UnderScoreUpperCaseAll: return camelCaseToUnderScore(string, false, true);
            case UnderScoreIgnoreDigitsUpperCaseAll: return camelCaseToUnderScore(string, true, true);
            case None:
            default: return string;
        }
    }

    public static String camelCaseToUnderScore (String string, boolean ignoreDigits, boolean upperCaseAll) {

        String formattedString = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
        if (formattedString == null) {
            formattedString = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
        }

        return upperCaseAll ? formattedString.toUpperCase() : formattedString;
    }

    public static String underScoreToCamelCase (String string, boolean upperCaseAll) {

        String lowerStr = lowercaseFirstChar(string);
        String formattedString = null;
        if (string.contains("_")) {
            formattedString = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, lowerStr);
        }
        if (formattedString == null) {
            formattedString = lowerStr;
        }
        return upperCaseAll ? formattedString.toUpperCase() : formattedString;
    }

    public static String capitalizeFirstChar (String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    public static String lowercaseFirstChar (String string) {
        return string.substring(0,1).toLowerCase() + string.substring(1);
    }

    public static String CapitalizedCamelCase (String string) {
        return capitalizeFirstChar(underScoreToCamelCase(string, false));
    }

    public static String splitStringForUppercaseComponents (String string, boolean excludeSingleChars) {
        String[] components = string.split("(?=\\p{Upper})");
        ArrayList<String> derivedComponents;

        if (excludeSingleChars) {
            ArrayList<String> excludedSingleCharComponents = new ArrayList();
            String currentStr = "";
            String correctedStr = "";

            for (int i = 0; i < components.length; i++) {
                currentStr = components[i];
                boolean isSingleChar = currentStr.length() <= 1;
                boolean isAdded = false;
                if (isSingleChar) {
                    correctedStr += currentStr;
                    isAdded = true;
                }
                if (isSingleChar && i < components.length - 1) {
                    continue;
                }
                else {
                    if (0 < correctedStr.length()) excludedSingleCharComponents.add(correctedStr);
                    if (!isAdded && 0 < currentStr.length()) excludedSingleCharComponents.add(currentStr);
                    correctedStr = "";
                }
            }
            derivedComponents = excludedSingleCharComponents;
        }
        else {
            derivedComponents = ArrayUtility.arrayList(components);
        }

        return ArrayUtility.componentsJoinedByString(derivedComponents, " ");
    }

    /** @brief Uses a regex format to validate string
     * @param format If format is null, returns true
     * @param maxChars If maxchars is 0, returns true */
    public static boolean hasValidCharacers (String string, Text.REGEX_FORMAT format, int maxChars) {
        if (StringUtility.isEmpty(string)) return true;
        if (format == null || format == Text.REGEX_FORMAT.DEFAULT) return true;
        if (maxChars == 0) return true;
        if (maxChars != Constants.NOT_FOUND_ID && maxChars < string.length()) return false;

        String regex = regexForFormat(format, maxChars);
        if (isEmpty(regex)) return true;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean hasValidCharacers (String string, String regex) {
        if (StringUtility.isEmpty(string)) return true;
        if (isEmpty(regex)) return true;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static String regexForFormat (Text.REGEX_FORMAT format, int length) {

        length = (length < 0 ? App.constants().Max_Regex_Chars() : length);
        switch (format) {
            case INT: return String.format("^([-]?[0-9]{0,%s})$", length);
            case INT_POSITIVE: return String.format("^([0-9]{0,%s})$", length);
            case FLOAT: return String.format("^[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$", length);
            case FLOAT_POSITIVE: return String.format("^([0-9]+([.][0-9]*)?|[.][0-9]+)$", length);
            case ALPHABET: return "^([a-zA-Z]+)$";
            case ALPHANUMERIC: return "^([a-zA-Z0-9]+)$";
            case PASSWORD: return App.constants().Regex_Password();
            case EMAIL: return App.constants().Regex_Email();
            case PHONE: return App.constants().Regex_Phone();
            default: return "";
        }
    }

    public static String quotations (String string) {
        return String.format("\"%s\"", string);
    }

    public static boolean isNotEmpty (Object string) {
        return string != null &&
                ((string instanceof String && ((String)string).length() > 0) ||
                        string.toString().length() > 0);
    }

    public static boolean isEmpty (Object string) {
        return !isNotEmpty(string);
    }

    public static String nonNull (String string) {
        return nonNull(string, "");
    }

    public static String nonNull(String string, String defaultText) {
        return string != null && !string.equalsIgnoreCase("null") ? string : defaultText;
    }

    public static String nonNull (Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static int positiveNumValue(String string) {
        String text = StringUtility.nonNull(string);
        try {
            return StringUtility.isNotEmpty(text) ? Integer.parseInt(text.replaceAll("[\\D]","")) : 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Number numValue (String string) {
        String text = StringUtility.nonNull(string);
        if (StringUtility.isEmpty(text)) return 0;

        try {
            return Float.parseFloat(text);
        }
        catch (NumberFormatException e) {}
        try {
            return Long.parseLong(text);
        }
        catch (NumberFormatException e) {}
        try {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e) {}
        return 0;
    }

    public static Float floatValue (String string) {
        String text = StringUtility.nonNull(string);
        if (StringUtility.isEmpty(text) || ".".equals(text)) return null;
        try {
            return Float.parseFloat(text);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float notNullFloatValue (String string) {
        String text = StringUtility.nonNull(string);
        if (StringUtility.isEmpty(text) || ".".equals(text)) return 0.0f;
        try {
            return Float.parseFloat(text);
        }
        catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public static long positiveLongValue(String string) {
        String text = StringUtility.nonNull(string);
        return StringUtility.isNotEmpty(text) ? Long.parseLong(text.replaceAll("[\\D]","")) : 0;
    }

    public static ArrayList<String> safeSplit (String string, String regex) {
        if (isNotEmpty(string)) return ArrayUtility.arrayList(string.split(regex));
        return new ArrayList<>();
    }

    public static boolean safeEquals (String obj1, String obj2) {
        return isNotEmpty(obj1) && obj1.equals(obj2);
    }

    public static String removeCharactersInSet (String s, Set<String> set) {

        if (StringUtility.isEmpty(s) || set == null) return s;
        String cleared = s;

        for (String string : set) {
            cleared = cleared.replace(string, "");
        }
        return cleared;
    }

    public static String removeCharacter (String s, String str) {

        Set<String> set = new HashSet<>();
        set.add(str);
        return removeCharactersInSet(s, set);
    }

    public static String numbersOnly (String s) {
        if (isEmpty(s)) return null;
        return s.replaceAll("[^0-9]", "");
    }

    public static boolean isNumbersOnly (String s) {
        if (isEmpty(s)) return false;
        return s.equals(numbersOnly(s));
    }

        /** Use range.length = 0 for ranges intended to the end of string */
    public static String substringWithRange (String text, int startIndex, int length) {

        Range range = Range.build(startIndex, length);
        if (Range.isEmpty(range)) return text;

        if (range.length == 0)
            return text.substring(range.location);
        else
            return text.substring(range.location, range.length+range.location);
    }

    /** Use range.length = 0 for ranges intended to the end of string */
    public static String substringWithRange (String text, Range range) {
        if (range == null) return "";
        if (range.length == 0)
            return text.substring(range.location);
        else
            return text.substring(range.location, range.length+range.location);
    }

    public static int height(String string, int fontSize, int width) {

        if (StringUtility.isEmpty(string)) return 0;
        if (width <= 0) return 0;

        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.FILL);

        Rect rect = new Rect();
        paint.getTextBounds(string, 0, string.length(), rect);
        int lines = (rect.width() / width) + 1;
        return rect.height() * lines;
    }

    public static int width(String string, int fontSize) {

        if (StringUtility.isEmpty(string)) return 0;

        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.FILL);

        Rect rect = new Rect();
        paint.getTextBounds(string, 0, string.length(), rect);
        return rect.width();
    }
}
