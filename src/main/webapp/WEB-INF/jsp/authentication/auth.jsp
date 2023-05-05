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
    <title>Tickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>

<body>
<div class="auth_block">
    <p>Для дальнейшей работы необходимо пройти авторизацию</p>

    <div class="buttons">
        <div><a href="${pageContext.request.contextPath}/registration">
            <button>Регистрация</button>
        </a></div>
        <div><a href="${pageContext.request.contextPath}/login">
            <button>Вход</button>
        </a></div>
    </div>
</div>
</body>
</html>
