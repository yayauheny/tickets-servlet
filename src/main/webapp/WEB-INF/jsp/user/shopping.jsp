<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manage Products</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<jsp:include page="header-user.jsp"/>

<section class="container">
    <h1 class="task-heading">Выберите нужные товары</h1>

    <form action="${pageContext.request.contextPath}/ticket?cardId=${sessionScope.cardId}" method="post">
        <table class="table">
            <thead>
            <tr>
                <th>Наименование</th>
                <th>Цена (${sessionScope.currency})</th>
                <th>Имеется в наличии</th>
                <th>Наличие скидки</th>
                <th>Количество</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td><c:out value="${product.name}"/></td>
                    <td><c:out value="${product.price}"/></td>
                    <td><c:out value="${product.quantity}"/></td>
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
                        <input type="hidden" name="productId" value="${product.id}">
                        <label>Кол-во:
                            <input type="number" name="quantity" step="1" value="0" min="0" max="${product.quantity}">
                        </label>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div class="user-options">
            <button type="submit" class="btn-refactor">Добавить все к покупкам</button>
        </div>
    </form>
</section>

</body>
</html>
