<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 18.04.2023
  Time: 6:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<div class="login_block">
    <p>Вход в аккаунт</p>

    <c:if test="${not empty userExists and !userExists}">
        <p style="color: red;">Ошибка при входе. Пользователь не найден.</p>
    </c:if>
    <c:if test="${not empty invalidPassword and invalidPassword}">
        <p style="color: red;">Ошибка при входе. Неверный пароль.</p>
    </c:if>

    <form class="login_form" action="${pageContext.request.contextPath}/login" method="post">
        <label>Почта:
            <input type="email" name="email">
        </label><br>

        <label>Пароль
            <input type="password" name="password">
        </label><br>

        <button type="submit">Войти</button>
    </form>
</div>
</body>
</html>
