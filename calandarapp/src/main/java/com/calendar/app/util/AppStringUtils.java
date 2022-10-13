package com.calendar.app.util;


import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

@UtilityClass
public class AppStringUtils implements Serializable {

    public static final String EMPTY = "";
    public static final String SEPARATORS = "[\\-_;:,.|*/]";

    public static boolean isNullOrEmpty(String param) {
        return param == null || param.isEmpty();
    }

    public static String exceptionToString(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
    public static boolean isBlank(String value) {
        return value == null || "".equals(value);
    }


    public static String escapeCharSequence(String arg, CharSequence charSequence) {
        if (!org.apache.commons.lang3.StringUtils.isEmpty(arg) && arg.contains(charSequence)) {
            return arg.replace(charSequence, "\\" + charSequence);
        }
        return arg;
    }
}