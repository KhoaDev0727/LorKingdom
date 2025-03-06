<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Promotion Added Successfully</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex justify-content-center align-items-center vh-100 bg-light">

    <div class="card shadow-lg p-4 text-center" style="width: 400px;">
        <h2 class="text-success mb-3">🎉 Success!</h2>
        <p class="mb-4">Promotion has been added successfully.</p>
        <a href="addPromotion.jsp" class="btn btn-primary mb-2">Add More</a>
        <a href="${pageContext.request.contextPath}/Admin/promotionController" class="btn btn-secondary">Back to Home</a>
    </div>

    <!-- Bootstrap JS (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
