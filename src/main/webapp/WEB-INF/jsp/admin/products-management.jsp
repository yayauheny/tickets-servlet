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
    <title>Manage Products</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<jsp:include page="header-admin.jsp"/>

<section class="container">
    <h1 class="task-heading">Список продуктов</h1>

    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Наименование</th>
            <th>Количество</th>
            <th>Цена (${sessionScope.currency})</th>
            <th>Наличие скидки</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td><c:out value="${product.id}"/></td>
                <td><c:out value="${product.name}"/></td>
                <td><c:out value="${product.quantity}"/></td>
                <td><c:out value="${product.price}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${product.discount}">
                            <c:out value="Да"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="Нет"/>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <div class="admin-options">
                        <form class="delete-option"
                              action="${pageContext.request.contextPath}/products?product-id=${product.id}&_method=delete"
                              method="post">
                            <button type="submit" class="btn-del">Удалить</button>
                        </form>

                        <form class="change-option"
                              action="${pageContext.request.contextPath}/products?product-id=${product.id}&_method=put"
                              method="post">
                            <div class="form-group">
                                <label for="name">Наименование:</label>
                                <input type="text" id="name" name="name" value="${product.name}"/>
                            </div>
                            <div class="form-group">
                                <label for="number">Количество:</label>
                                <input type="number" id="number" name="quantity" value="${product.quantity}"/>
                            </div>
                            <div class="form-group">
                                <label for="price">Цена:</label>
                                <input type="number" id="price" name="price" step="0.01" min="0" max="1000" required
                                       value="${product.price}"/>
                            </div>
                            <div class="form-group">
                                <label for="discount">Наличие скидки:</label>
                                <select id="discount" name="discount">
                                    <option value="true" ${product.discount == true ? 'selected' : ''}>Да</option>
                                    <option value="false" ${product.discount == false ? 'selected' : ''}>Нет</option>
                                </select>
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

