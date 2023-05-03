<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.05.2023
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
    <title>Users</title>
</head>
<body>

<div class="container">
    <div class="task-heading">Статистика</div>
    <table class="table">
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Почта</th>
            <th>Роль</th>
            <th>Скидочная карта</th>
            <th>Действие</th>
        </tr>
        <div class="container">
            <table class="table">
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td><c:out value="${user.id}"/></td>
                        <td><c:out value="${user.name}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td><c:out value="${user.role}"/></td>
                        <td><c:out value="${user.cardId}"/></td>
                        <td>
                            <form action="/users/user=${user.id}" method="post">
                                <input type="submit" class="btn-del" value="Удалить"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </table>
</div>

</body>
</html>
