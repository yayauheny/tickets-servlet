<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 18.04.2023
  Time: 6:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <form action="/registration" method="post">
        <label>Имя:
            <input type="text" name="name">
        </label><br>
        <label>Адрес:
            <input type="text" name="address">
        </label><br>
        <label>Почта:
            <input type="email" name="email">
        </label><br>
        <label>Пароль
            <input type="password" name="password">
        </label><br><label>Номер скидочной карты
            <input type="number" name="discountCard">
        </label><br>
        <label>Роль
            <select name="role">
                <option value="ADMIN">Администратор</option>
                <option value="CUSTOMER">Покупатель</option>
            </select>
        </label>
        <button type="submit">Подтвердить</button>
    </form>
</body>
</html>
