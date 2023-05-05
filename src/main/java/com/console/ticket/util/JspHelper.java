package com.console.ticket.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {
    public static final String JSP_FORMAT = "WEB-INF/jsp/%s/%s.jsp";

    public static String getPath(String dirName, String jspName) {
        return String.format(JSP_FORMAT, dirName, jspName);
    }
}
