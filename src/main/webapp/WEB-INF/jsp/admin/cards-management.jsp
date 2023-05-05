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
    <title>Manage Cards</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<jsp:include page="header-admin.jsp"/>

<section class="container">
    <h1 class="task-heading">Список скидочных карт</h1>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Размер скидки</th>
            <th>Действие</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${cards}" var="card">
            <tr>
                <td><c:out value="${card.id}"/></td>
                <td><c:out value="${card.discountSize}"/></td>
                <td>
                    <div class="admin-options">
                        <form class="delete-option"
                              action="${pageContext.request.contextPath}/cards?card-id=${card.id}&_method=delete"
                              method="post">
                            <button type="submit" class="btn-del">Удалить</button>
                        </form>

                        <form class="change-option"
                              action="${pageContext.request.contextPath}/cards?card-id=${card.id}&_method=put"
                              method="post">
                            <div class="form-group">
                                <label for="discount">Размер скидки:</label>
                                <input type="number" id="discount" name="discount-size" step="0.01" min="0" max="15" required
                                       value="${card.discountSize}"/>
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

