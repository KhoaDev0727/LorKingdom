<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="Model.Account" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="account" value="${sessionScope.account}" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <!-- jQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Material Design Icons -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/3.6.95/css/materialdesignicons.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <link rel="stylesheet" href="./CSS/styleProfileStaff.css">
        <title>Hồ sơ nhân viên</title>

    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.roleID}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <div class="container-fluid">
            <div class="sidebar">
                <div class="profile">
                    <div class="d-flex container profile-img">
                        <c:if test="${not empty account.image}">
                            <img style="width: 150px; height: 150px" src="${pageContext.request.contextPath}/${account.image}" alt="Avatar" class="avatar">
                        </c:if>
                        <span class="username">Xin chào, ${account.userName}</span>
                        <p style="color: white;">Vai trò:
                            <c:if test="${account.roleID eq 1}">ADMIN</c:if>
                            <c:if test="${account.roleID eq 2}"> STAFF
                            </c:if>
                            <c:if test="${account.roleID eq 4}"> WAREHOUSE
                            </c:if>
                            (ID: ${account.roleID})
                        </p>
                    </div>
                </div>
                <nav class="nav">
                    <ul>
                        <li><a href="#" id="profile-link"><i class="fas fa-user"></i> Quản lý hồ sơ</a></li>
                        <li><a href="
                               <c:choose>
                                   <c:when test='${sessionScope.roleID eq 2}'>${pageContext.request.contextPath}/Admin/CustomerManagementServlet</c:when>
                                   <c:when test='${sessionScope.roleID eq 4}'>${pageContext.request.contextPath}/Admin/ProductServlet?&action=list</c:when>
                                   <c:otherwise>FinancialDashboardServlet</c:otherwise>
                               </c:choose>" 
                               id="profile-link">
                                <i class="fas fa-chart-line"></i> Chuyển đến dashboard
                            </a>
                        </li>
                        <li><a href="LogoutPage" id="orders-link"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a></li>
                    </ul>
                </nav>
            </div>

            <div class="right-menu">
                <div id="profile-section">
                    <!-- staff profile -->
                    <div id="profile-section" class="container">
                        <div class="row align-items-center p-4 bg-light rounded shadow-sm">
                            <!-- staff Info -->
                            <div class="user-info col-md-8">
                                <div class="card p-4 border-0 shadow-sm">
                                    <h2 class="text-warning">Thông tin hồ sơ nhân viên</h2>
                                    <form action="ProfileStaffServlet" method="post" id="profileStaffForm">
                                        <div class="card-body">
                                            <p class="text-muted">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>

                                            <div class="mb-3">
                                                <p class="fw-bold">Email</p>
                                                <h6 class="text-secondary">${account.email}</h6>
                                            </div>

                                            <div class="mb-3">
                                                <p class="fw-bold">Trạng thái</p>
                                                <h6 class="text-secondary">${account.status}</h6>
                                            </div>

                                            <!-- Phone -->
                                            <div class="mb-3">
                                                <p class="fw-bold">Điện thoại</p>
                                                <div class="d-flex align-items-center">
                                                    <h6 id="displayPhone" class="me-2">${account.phoneNumber}</h6>
                                                    <input type="hidden" name="phoneNumber" id="phoneNumberInput"
                                                           value="${account.phoneNumber}">
                                                    <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                                       data-bs-toggle="modal" data-bs-target="#editPhoneModal">Thay đổi</a>
                                                </div>
                                            </div>

                                            <!-- Address -->
                                            <div class="mb-3">
                                                <p class="fw-bold">Địa chỉ</p>
                                                <div class="d-flex align-items-center">
                                                    <h6 id="displayAddress" class="me-2">${empty account.address ? "Địa chỉ
                                                                                           không có sẵn" : account.address}</h6>
                                                    <input type="hidden" name="address" id="addressInput"
                                                           value="${account.address}">
                                                    <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                                       data-bs-toggle="modal" data-bs-target="#editAddressModal">Thay
                                                        đổi</a>
                                                </div>
                                            </div>

                                            <!-- Password -->
                                            <div class="mb-3">
                                                <p class="fw-bold">Mật khẩu</p>
                                                <div class="d-flex align-items-center">
                                                    <h6 id="displayPassword" class="me-2">****</h6>
                                                    <input type="hidden" name="password" id="passwordInput" value="">
                                                    <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                                       data-bs-toggle="modal" data-bs-target="#editPasswordModal">Thay
                                                        đổi</a>
                                                </div>
                                            </div>

                                            <button type="submit" class="btn mt-3 w-100 background-button" style="color: white; font-weight: 550;">Cập nhật</button>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Avatar -->
                            <div class="col-md-4 text-center">
                                <div class="card shadow-sm p-4 d-flex justify-content-center align-items-center">
                                    <c:if test="${not empty account.image}">
                                        <img src="${pageContext.request.contextPath}/${account.image}" class="img-fluid rounded-circle border" width="150"
                                             alt="Avatar">
                                    </c:if>
                                    <form action="UpdateAvatarPage" method="post" enctype="multipart/form-data"
                                          class="mt-3 text-center">
                                        <label for="file-upload" class="btn btn-secondary">
                                            <i class="fas fa-camera"></i> Chọn ảnh
                                        </label>
                                        <input id="file-upload" type="file" name="avatar" accept="image/*" hidden>
                                        <button type="submit" class="btn mt-2 w-100 background-button" style="color: white; font-weight: 550;">Tải lên Avatar</button>
                                    </form>
                                </div>

                            </div>
                        </div>


                        <!-- Modal box -->
                        <div class="modal fade" id="editPhoneModal" tabindex="-1" aria-labelledby="editPhoneModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editPhoneModalLabel">Sửa số điện thoại</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <label for="newPhoneNumber" class="form-label">Số điện thoại mới:</label>
                                        <input type="text" id="newPhoneNumber" class="form-control"
                                               placeholder="Nhập số điện thoại mới">
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy
                                            bỏ</button>
                                        <button type="button" class="btn btn-save"
                                                onclick="updatePhoneNumber()">Lưu</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="editAddressModal" tabindex="-1" aria-labelledby="editAddressModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editAddressModalLabel">Sửa Địa Chỉ</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <label for="newAddress" class="form-label">Địa chỉ mới:</label>
                                        <input type="text" id="newAddress" class="form-control"
                                               placeholder="Nhập địa chỉ mới">
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy
                                            bỏ</button>
                                        <button type="button" class="btn btn-save" onclick="updateAddress()">Lưu</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="editPasswordModal" tabindex="-1"
                             aria-labelledby="editPasswordModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editPasswordModalLabel">Chỉnh sửa mật khẩu</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <label for="newPassword" class="form-label">Mật khẩu mới:</label>
                                        <input type="password" id="newPassword" class="form-control"
                                               placeholder="Nhập mật khẩu mới">
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy
                                            bỏ</button>
                                        <button type="button" class="btn btn-save" onclick="updatePassword()">Lưu</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Modal box -->
                    </div>
                    <!-- end profile -->
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                $('#profileStaffForm').on('submit', function (e) {
                    e.preventDefault(); // Ngăn submit mặc định

                    $.ajax({
                        url: 'ProfileStaffServlet',
                        type: 'POST',
                        data: $(this).serialize(),
                        dataType: 'json',
                        success: function (response) {
                            if (response.success) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Thành công!',
                                    text: response.message,
                                    showConfirmButton: false,
                                    timer: 1500
                                }).then(() => {
                                    // Cập nhật giao diện
                                    document.getElementById('displayPhone').innerText = document.getElementById('phoneNumberInput').value;
                                    document.getElementById('displayAddress').innerText = document.getElementById('addressInput').value || "Địa chỉ không có sẵn";
                                    if (document.getElementById('passwordInput').value) {
                                        document.getElementById('displayPassword').innerText = "****";
                                    }
                                });
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi!',
                                    text: response.message
                                });
                            }
                        },
                        error: function (xhr, status, error) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: 'Có lỗi xảy ra: ' + error
                            });
                        }
                    });
                });
            });

            function updatePhoneNumber() {
                let newPhone = document.getElementById("newPhoneNumber").value;
                if (newPhone.trim() !== "") {
                    document.getElementById("displayPhone").innerText = newPhone;
                    document.getElementById("phoneNumberInput").value = newPhone;
                    var phoneModal = bootstrap.Modal.getInstance(document.getElementById("editPhoneModal"));
                    phoneModal.hide();
                }
            }

            function updateAddress() {
                let newAddress = document.getElementById("newAddress").value;
                if (newAddress.trim() !== "") {
                    document.getElementById("displayAddress").innerText = newAddress;
                    document.getElementById("addressInput").value = newAddress;
                    var addressModal = bootstrap.Modal.getInstance(document.getElementById("editAddressModal"));
                    addressModal.hide();
                }
            }

            function updatePassword() {
                let newPassword = document.getElementById("newPassword").value;
                if (newPassword.trim() !== "") {
                    if (newPassword.length < 8) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Mật khẩu phải có ít nhất 8 ký tự!'
                        });
                        return; // Dừng hàm nếu mật khẩu không đủ dài
                    }
                    document.getElementById("displayPassword").innerText = "****";
                    document.getElementById("passwordInput").value = newPassword;
                    var passwordModal = bootstrap.Modal.getInstance(document.getElementById("editPasswordModal"));
                    passwordModal.hide();
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Mật khẩu đã được cập nhật!',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            }
        </script>
    </body>
</html>