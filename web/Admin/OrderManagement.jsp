<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Order Management</title>
        <!-- External CSS and JS -->
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 4}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <!-- Sidebar (Included) -->
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-5">
                        <h1 class="mt-4">Quản Lí Đơn Hàng</h1>

                        <!-- Thông báo Thành công/Lỗi -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
                            </div>
                        </c:if>

                        <!-- Biểu mẫu tìm kiếm trong một hàng -->
                        <div class="row mb-4">
                            <!-- Tìm kiếm theo tên khách hàng -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="search">
                                    <input type="text" name="customerName" class="form-control" placeholder="Nhập tên khách hàng" required>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-search"></i>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i>
                                    </a>
                                </form>
                            </div>

                            <!-- Tìm kiếm theo số tiền tối thiểu -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderView" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="money">
                                    <input type="text" name="minAmount" class="form-control" placeholder="Nhập số tiền tối thiểu" required>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-search"></i> 
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i>
                                    </a>
                                </form>
                            </div>

                            <!-- Sắp xếp đơn hàng -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="sort">
                                    <select name="sortOrder" class="form-control">
                                        <option value="ASC">Sắp xếp theo số tiền (Tăng dần)</option>
                                        <option value="DESC">Sắp xếp theo số tiền (Giảm dần)</option>
                                    </select>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-sort"></i> 
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i> 
                                    </a>
                                </form>
                            </div>
                        </div>

                        <!-- Order List Table -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <div class="d-flex justify-content-between align-items-center">
                                    <i class="fas fa-truck me-1"></i> Danh sách đơn hàng
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped table-hover">
                                        <thead class="table-dark text-center">
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th>Tên khách hàng</th>
                                                <th>Phương thức thanh toán</th>
                                                <th>Phương thức vận chuyển</th>
                                                <th>Ngày đặt hàng</th>
                                                <th>Trạng thái</th>
                                                <th>Tổng tiền</th>
                                                <th>Cập nhật</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listO}">
                                                    <tr>
                                                        <td colspan="8" class="text-center text-muted">No order found</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${listO}" var="o">
                                                        <tr>
                                                            <td>${o.orderId}</td>
                                                            <td>${o.accountName}</td>
                                                            <td>${o.payMentMethodName}</td>
                                                            <td>${o.shipingMethodName}</td>
                                                            <td>${o.orderDate}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test='${o.status == "Pending"}'>Chờ xử lý</c:when>
                                                                    <c:when test='${o.status == "Shipped"}'>Đã giao</c:when>
                                                                    <c:when test='${o.status == "Delivered"}'>Đã nhận hàng</c:when>
                                                                    <c:when test='${o.status == "Cancelled"}'>Đã hủy</c:when>
                                                                    <c:otherwise>${o.status}</c:otherwise> 
                                                                </c:choose>
                                                            </td>

                                                            <td>${o.totalAmount}</td>
                                                            <td>${o.updatedAt}</td>
                                                            <td>
                                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" onclick="setDeleteOrderID(${o.orderId})">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                                <a href="${pageContext.request.contextPath}/Admin/OrderView?action=view&orderId=${o.orderId}" class="btn btn-sm btn-info">
                                                                    <i class="fas fa-eye"></i>
                                                                </a>
                                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#updateStatusModal" 
                                                                        onclick="setUpdateOrderID(${o.orderId}, '${o.status}')">
                                                                    <i class="fas fa-edit"></i>
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <!-- Pagination -->
                        <nav class="pagination">
                            <ul class="pagination justify-content-center">
                                <c:forEach begin="1" end="${endPage}" var="i">
                                    <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                        <a class="page-link" href="${pageContext.request.contextPath}/Admin/OrderServlet?index=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>

                    </div>
                </main>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmDeleteModalLabel">Xác Nhận Xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa đơn hàng này không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form id="deleteForm" method="POST" action="OrderServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="orderId" id="deleteOrderID">
                            <button type="submit" class="btn btn-danger">Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- Update Status Modal -->
        <div class="modal fade" id="updateStatusModal" tabindex="-1" aria-labelledby="updateStatusModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updateStatusModalLabel">Cập nhật trạng thái đơn hàng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        <form id="updateStatusForm" action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="orderId" id="updateOrderID">

                            <div class="mb-3">
                                <label for="newStatus" class="form-label">Chọn trạng thái mới</label>
                                <select class="form-select" name="status" id="newStatus" required>
                                    <option value="Pending">Chờ xử lý</option>
                                    <option value="Shipped">Đã giao</option>
                                    <option value="Delivered">Đã nhận hàng</option>
                                    <option value="Cancelled">Đã hủy</option>
                                </select>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-primary">Cập nhật</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <% 
                 String successMessage = (String) session.getAttribute("successModal");
                 String errorMessage = (String) session.getAttribute("errorModal");
        %>
        <!-- Bootstrap Modal -->
        <div class="modal fade" id="notificationModal" tabindex="-1" aria-labelledby="notificationModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title text-center" id="notificationModalLabel">
                            <%= (successMessage != null) ? "Thành công" : "Lỗi" %>
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <p class="<%= (successMessage != null) ? "text-success" : "text-danger" %> fw-bold">
                            <%= (successMessage != null) ? successMessage : errorMessage %>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- Xóa thông báo khỏi session để không hiển thị lại --%>
        <% session.removeAttribute("successModal"); %>
        <% session.removeAttribute("errorModal"); %>

        <!-- Script to Set Order ID for Deletion -->
        <script>
            function setDeleteOrderID(orderID) {
                document.getElementById('deleteOrderID').value = orderID;
            }
        </script>
        <script>
            function setUpdateOrderID(orderID, currentStatus) {
                console.log("Order ID set for update:", orderID);  // Debug xem orderId có đúng không
                document.getElementById('updateOrderID').value = orderID;
                document.getElementById('newStatus').value = currentStatus;
            }
        </script>
        <!--        script open message modal-->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                var successMessage = "<%= successMessage %>";
                var errorMessage = "<%= errorMessage %>";

                if (successMessage.trim() !== "null" || errorMessage.trim() !== "null") {
                    var myModal = new bootstrap.Modal(document.getElementById("notificationModal"));
                    myModal.show();

                    // Tự động đóng modal sau 3 giây
                    setTimeout(() => {
                        myModal.hide();
                    }, 3000);
                }
            });
        </script>

    </body>
</html>


