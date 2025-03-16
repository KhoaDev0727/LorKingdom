<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Promotion List</title>
    <script>
        // Wait for the DOM to fully load before manipulating it
        document.addEventListener("DOMContentLoaded", function() {
            // Convert the sessionScope.listP to JSON and assign it to a JavaScript variable
            var listP = ${sessionScope.listP != null ? sessionScope.listP : '[]'};

            // Get the table body element
            var tableBody = document.getElementById('promotion-table-body');
            
            // Loop through the listP array and insert rows into the table
            listP.forEach(function(promotion) {
                var row = document.createElement('tr');
                row.innerHTML = `
                    <td>${promotion.name}</td>
                    <td>${promotion.promotionCode}</td>
                    <td>${promotion.discountPercent}%</td>
                    <td>${promotion.endDate}</td>
                `;
                tableBody.appendChild(row);
            });
        });
    </script>
</head>
<body>
    <div id="promotion-section" class="container">
        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark text-center">
                <tr>
                    <th>Name</th>
                    <th>Promotion Code</th>
                    <th>DiscountPercent (%)</th>  
                    <th>Expired date</th>                   
                </tr>
            </thead>
            <tbody id="promotion-table-body">      
                <!-- Data rows will be added dynamically via JavaScript -->
            </tbody>
        </table>
    </div>
</body>
</html>
