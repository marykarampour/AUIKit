package com.prometheussoftware.auikit.utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtility {

    public enum STYLE {
        NONE,
        DECIMAL,
        CURRENCY
    }

    public static String stringValueWithStyle (Number number, STYLE style, int digits) {
        switch (style) {
            case DECIMAL: {
                NumberFormat format = DecimalFormat.getInstance();
                format.setMinimumFractionDigits(digits);
                format.setMaximumFractionDigits(digits);
                format.setRoundingMode(RoundingMode.DOWN);
                return format.format(number);
            }
            case CURRENCY: {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setMinimumFractionDigits(digits);
                format.setMaximumFractionDigits(digits);
                format.setRoundingMode(RoundingMode.DOWN);
                return format.format(number);
            }
            default: {
                NumberFormat format = NumberFormat.getInstance();
                return format.format(number);
            }
        }
    }

    public static boolean isNumber(Class cls) {
        return Number.class.isAssignableFrom(cls) ||
                cls == short.class ||
                cls == int.class ||
                cls == long.class ||
                cls == double.class ||
                cls == float.class;
    }
}
