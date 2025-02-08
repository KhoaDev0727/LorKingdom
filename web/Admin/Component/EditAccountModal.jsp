<%-- 
    Document   : EditAccountModal
    Created on : Feb 8, 2025, 11:34:48 AM
    Author     : Truong Van Khang _ CE181852
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Edit Customer Modal -->
<div class="modal fade" id="editCustomerModal${s.accountId}">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form action="AdminManagementServlet" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="accountId" value="${s.accountId}">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Staff</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-12">
                            <label class="form-label">Upload Profile Image</label>
                            <input type="file" class="form-control" name="image" accept="image/*">
                            <!-- Giữ ảnh cũ nếu không upload mới -->
                            <input type="hidden" name="currentImage" value="${s.image}">
                            <c:if test="${not empty s.image}">
                                <img src="${pageContext.request.contextPath}/${s.image}" alt="Profile Image"
                                     class="mt-2 rounded" style="max-height: 100px;">
                            </c:if>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Full Name</label>
                            <input type="text" class="form-control" name="userName" 
                                   value="${s.userName}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Phone Number</label>
                            <input type="tel" class="form-control" name="phoneNumber" 
                                   value="${s.phoneNumber}" pattern="[0-9]{10}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control" name="email" 
                                   value="${s.email}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Password</label>
                            <input type="password" class="form-control" name="password" 
                                   value="${s.password}" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label">Address</label>
                            <textarea class="form-control" name="address" rows="2">${s.address}</textarea>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Status</label>
                            <select class="form-select" name="status">
                                <option value="Active" ${s.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${s.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                <option value="Blocked" ${s.status == 'Blocked' ? 'selected' : ''}>Blocked</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Role</label>
                            <select class="form-select" name="roleID">
                                <c:forEach var="r" items="${roles}">
                                    <option value="${r.roleID}" ${r.roleID == s.roleID ? 'selected' : ''}>
                                        ${r.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
