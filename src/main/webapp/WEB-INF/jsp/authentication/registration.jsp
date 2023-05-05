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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<div class="registration_block">
    <p>Заполните форму регистрации</p>

    <c:if test="${not empty isValid and !isValid}">
        <p style="color: red;">Ошибка при регистрации. Пожалуйста, заполните форму еще раз.</p>
    </c:if>

    <form class="registration_form" action="${pageContext.request.contextPath}/registration" method="post">
        <label>Имя:
            <input type="text" name="name">
        </label><br>

        <label>Почта:
            <input type="email" name="email">
        </label><br>

        <label>Пароль
            <input type="password" name="password">
        </label><br>

        <label>Роль
            <select name="role">
                <option value="ADMIN">Администратор</option>
                <option value="USER">Покупатель</option>
            </select>
        </label>

        <label>Номер скидочной карты
            <input type="text" name="discount_card">
        </label><br>

        <button type="submit">Зарегистрироваться</button>
    </form>
</div>
</body>
</html>
