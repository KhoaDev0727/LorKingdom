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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"
                integrity="sha384-a7sDzZnAdZTtR0sbvstjfGmVOBbQZT2H57t6QnAfXkCV5slDE45uJhxIMlLwMbdm"
        crossorigin="anonymous"></script>
        <!-- Material Design Icons -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/3.6.95/css/materialdesignicons.css"
              rel="stylesheet" integrity="sha384-21mpjRugMLP7O46ah5kbDHWZ5tnn+v7+SxQT6IihwMcrTz3FT5V5sQf3VNGiH3aJ"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="./assets/styleUser/styleProfile.css">
        <title>Hồ sơ</title>
    </head>
    <body>
        <div class="page-content" id="page-content">
            <div class="padding">
                <div class="container d-flex justify-content-center">
                    <div class="card user-card-full">
                        <div class="row box-content">
                            <!-- user-profile -->
                            <div class="bg-c-lite-green user-profile">
                                <div class="text-center text-white">
                                    <div class="avatar-container">
                                        <c:if test="${not empty account.image}">
                                            <img src="${account.image}" alt="Avatar" class="avatar">
                                        </c:if>

                                        <form action="UpdateAvatarServlet" method="post" enctype="multipart/form-data">
                                            <label for="file-upload" class="custom-file-upload">
                                                <i class="fas fa-camera"></i>
                                            </label>
                                            <input id="file-upload" type="file" name="avatar" accept="image/*" required
                                                   hidden>
                                            <button type="submit">Tải lên Avatar</button>
                                        </form>
                                    </div>

                                    <h6 class="f-w-600 title" style="font-size: 20px;">Xin chào, ${account.userName}</h6>
                                    <i class="mdi mdi-square-edit-outline feather icon-edit m-t-10 f-16"></i>
                                </div>
                            </div>



                            <!-- Card block -->
                            <div class="user-info">
                                <form action="ProfileServlet" method="post">
                                    <div class="right">
                                        <div class="card-block">
                                            <h3 class="">Thông tin hồ sơ</h3>
                                            <div class="">
                                                <div class="">
                                                    <p class="">Email</p>
                                                    <h6 class="">${account.email}</h6>
                                                </div>
                                                <div class="">
                                                    <p class="">Trạng thái</p>
                                                    <h6 class="">${account.status}</h6>
                                                </div>
                                                <div class="">
                                                    <p class="">Số dư</p>
                                                    <h6 class="">${account.balance}</h6>
                                                </div>


                                                <!-- Phone -->
                                                <div class="">
                                                    <p class="">Điện thoại</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayPhone">${account.phoneNumber}</h6>
                                                        <input type="hidden" name="phoneNumber" id="phoneNumberInput" value="${account.phoneNumber}">
                                                        <a href="#" class="text-primary text-decoration-none change-btn"
                                                           data-bs-toggle="modal" data-bs-target="#editPhoneModal">Thay đổi</a>
                                                    </div>
                                                </div>

                                                <!-- Address -->
                                                <div class="">
                                                    <p class="">Địa chỉ</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayAddress">${empty account.address ? "Địa chỉ không có sẵn" : account.address}</h6>
                                                        <input type="hidden" name="address" id="addressInput" value="${account.address}">
                                                        <a href="#" class="text-primary text-decoration-none change-btn"
                                                           data-bs-toggle="modal" data-bs-target="#editAddressModal">Thay đổi</a>
                                                    </div>
                                                </div>

                                                <!-- Password -->
                                                <div class="">
                                                    <p class="">Mật khẩu</p>
                                                    <div class="d-flex">
                                                        <h6 id="displayPassword">${account.password}</h6> <!-- Hiển thị **** -->
                                                        <input type="hidden" name="password" id="passwordInput" value="">
                                                        <a href="#" class="text-primary text-decoration-none change-btn" data-bs-toggle="modal" data-bs-target="#editPasswordModal">Thay đổi</a>
                                                    </div>
                                                </div>

                                            </div>

                                            <button type="submit btn-update">Cập nhật</button>
                                        </div>
                                    </div>
                                </form>
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
                                            <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Hủy bỏ</button>
                                            <button type="button" class="btn btn-save"
                                                    onclick="updatePhoneNumber()">Lưu</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="modal fade" id="editAddressModal" tabindex="-1"
                                 aria-labelledby="editAddressModalLabel" aria-hidden="true">
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
                                            <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Hủy bỏ</button>
                                            <button type="button" class="btn btn-save"
                                                    onclick="updateAddress()">Lưu</button>
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
                    document.getElementById("displayPassword").innerText = "****"; // Luôn hiển thị placeholder
                    document.getElementById("passwordInput").value = newPassword; // Lưu mật khẩu mới để gửi form
                    var passwordModal = bootstrap.Modal.getInstance(document.getElementById("editPasswordModal"));
                    passwordModal.hide();
                }
            }
        </script>


    </body>
</html>
