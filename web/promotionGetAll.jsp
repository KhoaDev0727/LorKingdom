<%-- 
    Document   : promotionGetAll
    Created on : Mar 16, 2025, 8:47:00 AM
    Author     : ROG SRIX
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="profile-section" class="container">
            <table class="table table-bordered table-striped table-hover">
                <thead class="table-dark text-center">
                    <tr>
                        <th>Name</th>
                        <th>Promotion Code</th>
                        <th>DiscountPercent (%)</th>  
                        <th>Expired date</th>                   
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listAll}" var="o">
                        <tr>
                            <td>${o.name}</td>
                            <td>${o.promotionCode}</td>
                            <td>${o.discountPercent}%</td>
                            <td>${o.endDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
