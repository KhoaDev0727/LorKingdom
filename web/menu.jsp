<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh Mục</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            /* Tùy chỉnh thanh cuộn */
            .scrollbar::-webkit-scrollbar {
                width: 8px;
            }
            .scrollbar::-webkit-scrollbar-thumb {
                background-color: rgba(107, 114, 128, 0.5); /* Xám nhạt */
                border-radius: 4px;
            }
            .scrollbar::-webkit-scrollbar-thumb:hover {
                background-color: rgba(107, 114, 128, 0.8); /* Xám đậm khi hover */
            }
        </style>
    </head>
    <body class="bg-gray-100 text-gray-900">
        <div class="container mx-auto py-6">
            <!-- Tiêu đề -->
            <h1 class="text-2xl font-bold text-red-600 mb-4">Danh Mục</h1>

            <!-- Thông báo lỗi nếu có -->
            <c:if test="${not empty errorMessage}">
                <div class="bg-yellow-100 text-yellow-800 p-4 rounded-md mb-4">
                    ${errorMessage}
                </div>
            </c:if>

            <!-- Hiển thị danh mục -->
            <c:if test="${empty errorMessage}">
                <div class="bg-white shadow-md rounded-md p-4 max-h-96 overflow-y-auto scrollbar">
                    <!-- Danh mục chính -->
                    <ul class="divide-y divide-gray-200">
                        <c:forEach var="superCategory" items="${superCategories}">
                            <!-- SuperCategory -->
                            <li class="py-3 flex justify-between items-center">
                                <div>
                                    <span class="font-medium">${superCategory.name}</span>
                                    <span class="text-gray-500 font-light ml-2">(${superCategory.totalItems})</span>
                                </div>
                                <span class="text-gray-400">&gt;</span> <!-- Icon mũi tên -->
                            </li>

                            <!-- Danh mục con -->
                            <ul class="ml-4 pl-4 border-l-2 border-gray-200">
                                <c:forEach var="category" items="${categories}">
                                    <c:if test="${category.superCategoryID == superCategory.superCategoryID}">
                                        <li class="py-2 text-sm text-gray-700 hover:text-red-500">
                                            <a href="#">${category.name}</a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
        </div>
    </body>
</html>
