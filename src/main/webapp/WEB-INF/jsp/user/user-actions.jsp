<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.05.2023
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>

<body>
<jsp:include page="header-user.jsp"/>

<div class="page-message">
    <p>Уважаемый покупатель, добро пожаловать в систему
        Перечень доступных операций доступен по ссылкам в оглавлении
    </p>
</div>
</body>
</html>
