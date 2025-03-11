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
        <c:if test="${empty sessionScope.roleID}">
            <c:redirect url="/Admin/loginPage.jsp"/>
        </c:if>
        <div class="page-content" id="page-content">
            <div class="padding">
                <div class="container d-flex justify-content-center">
                    <div class="card user-card-full">
                        <div class="row box-content">
                            <!-- user-profile -->
                            <div class="bg-c-lite-green user-profile d-flex flex-column">
                                <div class="text-center text-white">
                                    <div class="avatar-container">
                                        <div class="avatar-container">
                                            <c:if test="${not empty account.image}">
                                                <img src="${pageContext.request.contextPath}/${account.image}" alt="Avatar" class="avatar">
                                            </c:if>
                                            <form action="UpdateAvatarPage" method="post" enctype="multipart/form-data">
                                                <input type="hidden" name="currentImage" value="${account.image}">
                                                <label for="file-upload" class="custom-file-upload">
                                                    <i class="fas fa-camera"></i>
                                                </label>
                                                <input id="file-upload" type="file" name="avatar" accept="image/*" required hidden>
                                                <button type="submit">Tải lên Avatar</button>
                                            </form>
                                        </div>
                                    </div>
                                    <h6 class="f-w-600 title" style="font-size: 20px;">Xin chào, ${account.userName}</h6>
                                    <p>Vai trò: Staff (ID: ${account.roleID})</p>     
                                </div>

                                <!-- Nút Về trang chủ nằm dưới cùng -->
                                <div class="d-flex justify-content-center mt-auto">
                                    <a href="${pageContext.request.contextPath}/Admin/DashBoard.jsp" class="btn" style="background: linear-gradient(135deg, #20c997, #17a2b8); color: white;">
                                        <i class="fas fa-arrow-left"></i> Về trang chủ
                                    </a>
                                </div>

                            </div>

                            <!-- Card block -->
                            <div class="user-info">
                                <form action="ProfileStaffServlet" method="post" id="profileStaffForm">
                                    <div class="right">
                                        <div class="card-block">
                                            <h3 class="">Thông tin hồ sơ nhân viên</h3>
                                            <div class="">
                                                <div class="">
                                                    <p class="">Email</p>
                                                    <h6 class="">${account.email}</h6>
                                                </div>
                                                <div class="">
                                                    <p class="">Trạng thái</p>
                                                    <h6 class="">${account.status}</h6>
                                                </div>
                                                <!-- Phone -->
                                                <div class="">
                                                    <p class="">Điện thoại</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayPhone">${account.phoneNumber}</h6>
                                                        <input type="hidden" name="phoneNumber" id="phoneNumberInput" value="${account.phoneNumber}">
                                                        <a href="#" class="text-primary text-decoration-none change-btn" data-bs-toggle="modal" data-bs-target="#editPhoneModal">Thay đổi</a>
                                                    </div>
                                                </div>
                                                <!-- Address -->
                                                <div class="">
                                                    <p class="">Địa chỉ</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayAddress">${empty account.address ? "Địa chỉ không có sẵn" : account.address}</h6>
                                                        <input type="hidden" name="address" id="addressInput" value="${account.address}">
                                                        <a href="#" class="text-primary text-decoration-none change-btn" data-bs-toggle="modal" data-bs-target="#editAddressModal">Thay đổi</a>
                                                    </div>
                                                </div>
                                                <!-- Password -->
                                                <div class="">
                                                    <p class="">Mật khẩu</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayPassword">****</h6>
                                                        <input type="hidden" name="password" id="passwordInput" value="">
                                                        <a href="#" class="text-primary text-decoration-none change-btn" data-bs-toggle="modal" data-bs-target="#editPasswordModal">Thay đổi</a>
                                                    </div>
                                                </div>
                                            </div>
                                            <button type="submit" class="btn-update">Cập nhật</button>

                                        </div>
                                    </div>
                                </form>
                            </div>

                            <!-- Modal box -->
                            <div class="modal fade" id="editPhoneModal" tabindex="-1" aria-labelledby="editPhoneModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editPhoneModalLabel">Sửa số điện thoại</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <label for="newPhoneNumber" class="form-label">Số điện thoại mới:</label>
                                            <input type="text" id="newPhoneNumber" class="form-control" placeholder="Nhập số điện thoại mới">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                                            <button type="button" class="btn btn-save" onclick="updatePhoneNumber()">Lưu</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal fade" id="editAddressModal" tabindex="-1" aria-labelledby="editAddressModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editAddressModalLabel">Sửa Địa Chỉ</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <label for="newAddress" class="form-label">Địa chỉ mới:</label>
                                            <input type="text" id="newAddress" class="form-control" placeholder="Nhập địa chỉ mới">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                                            <button type="button" class="btn btn-save" onclick="updateAddress()">Lưu</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal fade" id="editPasswordModal" tabindex="-1" aria-labelledby="editPasswordModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editPasswordModalLabel">Chỉnh sửa mật khẩu</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <label for="newPassword" class="form-label">Mật khẩu mới:</label>
                                            <input type="password" id="newPassword" class="form-control" placeholder="Nhập mật khẩu mới">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                                            <button type="button" class="btn btn-save" onclick="updatePassword()">Lưu</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Modal box -->
                        </div>
                    </div>
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
                    if (newPassword.length < 6) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Mật khẩu phải có ít nhất 6 ký tự!'
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