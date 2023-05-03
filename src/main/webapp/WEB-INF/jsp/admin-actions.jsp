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
    <title>Administrator</title>

</head>

<body>
<header>
    <div class="head-content">
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/users">Управление пользователями</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Управление товарами</a></li>
                <li><a href="${pageContext.request.contextPath}/cards">Управление картами</a></li>
            </ul>
        </nav>
    </div>
</header>
<div class="page-message">
    <p>Администратор, добро пожаловать в систему
        Перечень
    </p>
</div>
</body>
</html>
