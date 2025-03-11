<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.Account" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="account" value="${sessionScope.account}" />

<!-- profile -->
<div id="profile-section" class="container">
    <div class="row align-items-center p-4 bg-light rounded shadow-sm">
        <!-- User Info -->
        <div class="user-info col-md-8">
            <div class="card p-4 border-0 shadow-sm">
                <h2 class="text-warning">Hồ Sơ Của Tôi</h2>
                <form action="ProfileServlet" method="post" id="profileForm">
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
                                <input type="hidden" name="phoneNumber" id="phoneNumberInput" value="${account.phoneNumber}">
                                <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                   data-bs-toggle="modal" data-bs-target="#editPhoneModal">Thay đổi</a>
                            </div>
                        </div>

                        <!-- Address -->
                        <div class="mb-3">
                            <p class="fw-bold">Địa chỉ</p>
                            <div class="d-flex align-items-center">
                                <h6 id="displayAddress" class="me-2">${empty account.address ? "Địa chỉ không có sẵn" : account.address}</h6>
                                <input type="hidden" name="address" id="addressInput" value="${account.address}">
                                <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                   data-bs-toggle="modal" data-bs-target="#editAddressModal">Thay đổi</a>
                            </div>
                        </div>

                        <!-- Password -->
                        <div class="mb-3">
                            <p class="fw-bold">Mật khẩu</p>
                            <div class="d-flex align-items-center">
                                <h6 id="displayPassword" class="me-2">****</h6>
                                <input type="hidden" name="password" id="passwordInput" value="">
                                <a href="#" class="text-primary text-decoration-none change-btn fw-bold"
                                   data-bs-toggle="modal" data-bs-target="#editPasswordModal">Thay đổi</a>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary mt-3 w-100">Cập nhật</button>
                        <button type="button" class="btn btn-danger mt-3 w-100" data-bs-toggle="modal" data-bs-target="#deactivateModal">Vô hiệu hóa tài khoản</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Avatar -->
        <div class="col-md-4 text-center">
            <div class="card shadow-sm p-4 d-flex justify-content-center align-items-center">
                <c:if test="${not empty account.image}">
                    <img src="${account.image}" class="img-fluid rounded-circle border" width="150" alt="Avatar">
                </c:if>
                <form action="UpdateAvatarServlet" method="post" enctype="multipart/form-data" class="mt-3 text-center">
                    <label for="file-upload" class="btn btn-secondary">
                        <i class="fas fa-camera"></i> Chọn ảnh
                    </label>
                    <input id="file-upload" type="file" name="avatar" accept="image/*" hidden>
                    <button type="submit" class="btn btn-primary mt-2 w-100">Tải lên Avatar</button>
                </form>
            </div>

        </div>
    </div>


    <!-- Modal xác nhận vô hiệu hóa tài khoản -->
    <div class="modal fade" id="deactivateModal" tabindex="-1" aria-labelledby="deactivateModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deactivateModalLabel">Xác nhận vô hiệu hóa tài khoản</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p class="text-danger">Bạn có chắc chắn muốn vô hiệu hóa tài khoản không? Hành động này sẽ khiến tài khoản của bạn ngừng hoạt động và bạn sẽ không thể đăng nhập được nữa.</p>
                    <label for="deactivatePassword" class="form-label">Nhập mật khẩu để xác nhận:</label>
                    <input type="password" id="deactivatePassword" class="form-control" placeholder="Nhập mật khẩu của bạn">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-danger" id="confirmDeactivateBtn" disabled onclick="deactivateAccount()">Vô hiệu hóa</button>
                </div>
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
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-save" onclick="updatePhoneNumber()">Lưu</button>
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
                    <input type="text" id="newAddress" class="form-control" placeholder="Nhập địa chỉ mới">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-save" onclick="updateAddress()">Lưu</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editPasswordModal" tabindex="-1" aria-labelledby="editPasswordModalLabel"
         aria-hidden="true">
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
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-save" onclick="updatePassword()">Lưu</button>
                </div>
            </div>
        </div>
    </div>
    <!-- Modal box -->
</div>
<!-- end profile -->

<script>
    $(document).ready(function () {
        $('#profileForm').on('submit', function (e) {
            e.preventDefault(); // Ngăn submit mặc định

            $.ajax({
                url: 'ProfileServlet',
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
                            document.getElementById('displayUserName').innerText = document.getElementById('userNameInput').value;
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

<script>
    // Kích hoạt nút "Vô hiệu hóa" khi người dùng nhập mật khẩu
    document.getElementById('deactivatePassword').addEventListener('input', function () {
        const confirmDeactivateBtn = document.getElementById('confirmDeactivateBtn');
        if (this.value.trim() !== '') {
            confirmDeactivateBtn.disabled = false;
        } else {
            confirmDeactivateBtn.disabled = true;
        }
    });

// Hàm xử lý vô hiệu hóa tài khoản
    function deactivateAccount() {
        const password = document.getElementById('deactivatePassword').value;

        if (password.trim() === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Cảnh báo!',
                text: 'Vui lòng nhập mật khẩu để xác nhận.'
            });
            return;
        }

        // Gửi yêu cầu AJAX đến servlet
        $.ajax({
            url: 'DeactivateAccountServlet',
            type: 'POST',
            data: {
                password: password
            },
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
                        // Đăng xuất người dùng và chuyển hướng về trang đăng nhập
                        window.location.href = 'LogoutServlet';
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
    }
</script>
