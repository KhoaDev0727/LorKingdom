<tr class="${category.isDeleted == 1 ? 'deleted' : ''}">
    <td>${category.superCategoryID}</td>
    <td>${category.name}</td>
    <td>
        <c:choose>
            <c:when test="${category.isDeleted == 1}">
                <span class="badge bg-secondary">Deleted</span>
            </c:when>
            <c:otherwise>
                <span class="badge bg-success">Active</span>
            </c:otherwise>
        </c:choose>
    </td>
    <td>${category.createdAt}</td>
    <td>
        <button class="btn btn-sm btn-warning" 
            data-bs-toggle="modal" 
            data-bs-target="#editCategoryModal-${category.superCategoryID}"
            ${category.isDeleted == 1 ? 'disabled' : ''}>
            <i class="fas fa-edit"></i>
        </button>
        <!-- Th�m ?i?u ki?n ?? kh�ng hi?n th? n�t x�a n?u ?� x�a -->
        <c:if test="${category.isDeleted == 0}">
            <form method="post" action="SuperCategoryServlet" class="d-inline">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="superCategoryID" value="${category.superCategoryID}">
                <button type="submit" class="btn btn-sm btn-danger">
                    <i class="fas fa-trash"></i>
                </button>
            </form>
        </c:if>
        <!-- N�t kh�i ph?c -->
        <c:if test="${category.isDeleted == 1}">
            <button class="btn btn-sm btn-success" onclick="restoreCategory(${category.superCategoryID})">
                Restore
            </button>
        </c:if>
    </td>
</tr>
