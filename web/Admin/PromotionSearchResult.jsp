<%@ page import="java.util.List" %>
<%@ page import="Model.Promotion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả tìm kiếm khuyến mãi</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome for Icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                margin-top: 30px;
            }
            .table th {
                background-color: #007bff;
                color: white;
                text-align: center;
            }
            .table td {
                text-align: center;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="card shadow p-4">
                <h2 class="text-center text-primary mb-4">
                    <i class="fas fa-tags"></i> Danh Sách Mã Giảm Giá
                </h2>
                <h5 class="text-center mb-3">Khuyến mãi có mức giảm giá cao hơn <strong><%= request.getAttribute("minDiscount") %>%</strong></h5>

                <%
                    List<Promotion> promotionList = (List<Promotion>) request.getAttribute("promotionList");
                    if (promotionList != null && !promotionList.isEmpty()) {
                %>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Tên Mã Giảm Giá</th>
                                <th>Mô Tả</th>
                                <th>% Giảm giá</th>
                                <th>Ngày Bắt Đầu</th>
                                <th>Ngày Hết Hạng</th>
                                <th>Trạng Thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (Promotion p : promotionList) {
                            %>
                            <tr>
                                <td><%= p.getPromotionID() %></td>
                                <td><%= p.getName() %></td>
                                <td><%= p.getDescription() %></td>
                                <td class="fw-bold text-danger"><%= p.getDiscountPercent() %>%</td>
                                <td><%= p.getStartDate() %></td>
                                <td><%= p.getEndDate() %></td>
                                <td>
                                    <span class="badge <%= p.getStatus().equals("Active") ? "bg-success" : "bg-danger" %>">
                                        <%= p.getStatus() %>
                                    </span>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
                <%
                    } else {
                %>
                <div class="alert alert-warning text-center">
                    <i class="fas fa-exclamation-circle"></i> Không tìm thấy khuyến mãi nào có mức giảm giá cao hơn. <strong><%= request.getAttribute("minDiscount") %>%</strong>.
                </div>
                <%
                    }
                %>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/Admin/promotionController" class="btn btn-outline-primary">
                        <i class="fas fa-arrow-left"></i> Quay lại trang chủ
                    </a>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
