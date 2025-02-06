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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-pzjw8f+ua7Kw1TIq0p0eY3wOX0x3M2Kh35g2WQ1Gz1KuK5gquVXfakjpREUpn7I4" crossorigin="anonymous">
        <!-- Bootstrap 5 JS Bundle (includes Popper.js) -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-pzjw8f+ua7Kw1TIq0p0eY3wOX0x3M2Kh35g2WQ1Gz1KuK5gquVXfakjpREUpn7I4"
        crossorigin="anonymous"></script>
        <!-- jQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"
                integrity="sha384-a7sDzZnAdZTtR0sbvstjfGmVOBbQZT2H57t6QnAfXkCV5slDE45uJhxIMlLwMbdm"
        crossorigin="anonymous"></script>
        <!-- Material Design Icons -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/MaterialDesign-Webfont/3.6.95/css/materialdesignicons.css"
              rel="stylesheet" integrity="sha384-21mpjRugMLP7O46ah5kbDHWZ5tnn+v7+SxQT6IihwMcrTz3FT5V5sQf3VNGiH3aJ"
              crossorigin="anonymous">
        <link rel="stylesheet" href="./assets/styleUser/styleProfile.css">
        <title>Profile</title>
    </head>
    <body>
        <div class="page-content" id="page-content">
            <div class="padding">
                <div class="container d-flex justify-content-center">
                    <div class="card user-card-full">
                        <div class="row box-content">
                            <!-- user-profile -->
                            <div class="col-4 bg-c-lite-green user-profile">
                                <div class="text-center text-white">

                                    <div class="m-b-25">
                                        <c:if test="${not empty account.image}">
                                            <img src="${account.image}" alt="Avatar" width="150px">
                                        </c:if>
                                    </div>

                                    <form action="UpdateAvatarServlet" method="post" enctype="multipart/form-data">
                                        <!-- Label giúp bấm vào để mở hộp thoại chọn file -->
                                        <label for="file-upload" class="custom-file-upload">Choose Avatar</label>
                                        <input id="file-upload" type="file" name="avatar" accept="image/*" required hidden>

                                        <!-- Hiển thị tên file đã chọn -->
                                        <span id="file-name"></span>

                                        <button type="submit">
                                            Upload Avatar
                                        </button>
                                    </form>

                                 
                                    <h6 class="f-w-600 text-black" style="font-size: 25px;">Hello, ${account.userName} </h6>
                                    <i class=" mdi mdi-square-edit-outline feather icon-edit m-t-10 f-16"></i>
                                </div>
                            </div>

                            <!-- Card block -->
                            <form action="ProfileServlet" method="post">
                                <div class="col-8 right">
                                    <div class="card-block">
                                        <h3 class="m-b-20 p-b-5 b-b-default f-w-700">Profile Information</h3>
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <p class="m-b-10">Email</p>
                                                <h6 class="f-w-400">${account.email}</h6>
                                            </div>
                                            <div class="col-sm-6">
                                                <p class="m-b-10">Phone</p>
                                                <input type="text" name="phoneNumber" value="${account.phoneNumber}" required>
                                            </div>
                                            <div class="col-sm-6">
                                                <p class="m-b-10">Status</p>
                                                <h6 class="f-w-400">${account.status}</h6>
                                            </div>
                                            <div class="col-sm-6">
                                                <p class="m-b-10">Balance</p>
                                                <h6 class="f-w-400">${account.balance}</h6>
                                            </div>
                                            <div class="col-sm-6">
                                                <p class="m-b-10">Address</p>
                                                <h6 class="f-w-400">
                                                    <c:choose>
                                                        <c:when test="${not empty account.address}">
                                                            ${account.address}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Address not available
                                                        </c:otherwise>
                                                    </c:choose>
                                                </h6>
                                                <input type="text" name="address" value="${account.address}">
                                            </div>
                                        </div>

                                        <!-- Chỉ có một nút Update duy nhất -->
                                        <button type="submit btn-all">Update</button>
                                    </div>
                                </div>
                            </form>



                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
