package com.console.ticket.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServletsUtil {
    public static String getStringParameterFromRequest(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return req.getParameter(parameter);
    }

    public static boolean getBooleanParameterFromRequest(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return Boolean.getBoolean(parameter);
    }

    public static double getDoubleParameterFromRequest(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return Double.parseDouble(parameter);
    }

    public static int getIntegerParameterFromRequest(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return Integer.parseInt(parameter);
    }
}
