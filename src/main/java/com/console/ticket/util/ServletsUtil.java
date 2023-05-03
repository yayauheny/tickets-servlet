package com.console.ticket.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServletsUtil {

    public static final String HTTP_CONTENT_TYPE_JSON = "application/json";
    public static String HTTP_CONTENT_TYPE_PDF = "application/pdf";
    public static final String HTTP_CONTENT_TYPE_PLAINTEXT = "text/plain";
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_DEFAULT_PAGE_SIZE = 3;

    public static void configureResponse(HttpServletResponse resp, String contentType, int statusCode) {
        resp.setContentType(contentType);
        resp.setStatus(statusCode);
    }

    public static String getStringParameterFromRequest(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return parameter;
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

    public static int getIntegerParameterFromReq(HttpServletRequest req, String paramName) throws NumberFormatException {
        String parameter = req.getParameter(paramName);

        if (parameter == null) {
            throw new NumberFormatException();
        }

        return Integer.parseInt(parameter);
    }
}
