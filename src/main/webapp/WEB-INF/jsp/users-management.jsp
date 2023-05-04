<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.05.2023
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manage Users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<jsp:include page="header-admin.jsp"/>

<section class="container">
    <h1 class="task-heading">Список пользователей</h1>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Почта</th>
            <th>Роль</th>
            <th>Скидочная карта</th>
            <th>Действие</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td><c:out value="${user.role}"/></td>
                <td><c:out value="${user.cardId}"/></td>
                <td>
                    <div class="admin-options">
                        <form class="delete-option"
                              action="${pageContext.request.contextPath}/users?user-id=${user.id}&_method=delete"
                              method="post">
                            <button type="submit" class="btn-del">Удалить</button>
                        </form>

                        <form class="change-option"
                              action="${pageContext.request.contextPath}/users?user-id=${user.id}&_method=put"
                              method="post">
                            <div class="form-group">
                                <input type="hidden" name="password" value="${user.password}"/>
                            </div>
                            <div class="form-group">
                                <label for="name">Имя:</label>
                                <input type="text" id="name" name="name" value="${user.name}"/>
                            </div>
                            <div class="form-group">
                                <label for="email">Почта:</label>
                                <input type="email" id="email" name="email" value="${user.email}"/>
                            </div>
                            <div class="form-group">
                                <label for="role">Роль:</label>
                                <select id="role" name="role">
                                    <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Администратор
                                    </option>
                                    <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>Покупатель</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="discount-card">Номер скидочной карты:</label>
                                <input type="text" id="discount-card" name="discount_card" size="8"
                                       value="${user.cardId}"
                                       class="discount-input"/>
                            </div>
                            <button type="submit" class="btn-refactor">Изменить</button>
                        </form>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>

</body>
</html>
