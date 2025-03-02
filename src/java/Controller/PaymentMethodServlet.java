package Controller;

import DAO.PaymentDAO;
import DBConnect.DBConnection;
import Model.Payment;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/Admin/PaymentMethodServlet")
public class PaymentMethodServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.equals("list")) {
                listPaymentMethods(request, response);
            } else {
                switch (action) {
                    case "add":
                        addPaymentMethod(request, response);
                        break;
                         case "listDeleted":
                        listDeletedPaymentMethods(request, response);
                        break;
                    case "update":
                        updatePaymentMethod(request, response);
                        break;
                    case "delete": // Xóa mềm
                        softDeletePaymentMethod(request, response);
                        break;
                    case "hardDelete": // Xóa cứng
                        hardDeletePaymentMethod(request, response);
                        break;
                    case "restore":
                        restorePaymentMethod(request, response);
                        break;
                    case "search":
                        searchPaymentMethods(request, response);
                        break;
                    default:
                        listPaymentMethods(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("Admin/error.jsp").forward(request, response);
        }
    }

    private void listPaymentMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Payment> paymentMethods = paymentDAO.getAllPaymentMethods();
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("PaymentMethodManagement.jsp").forward(request, response);
    }
    
    private void listDeletedPaymentMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        PaymentDAO paymentDAO = new PaymentDAO(); // Khởi tạo đối tượng DAO
        List<Payment> paymentMethods = paymentDAO.getDeletedPaymentMethod();
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("PaymentMethodManagement.jsp").forward(request, response);
    }

    private void addPaymentMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String methodName = request.getParameter("methodName");
        String description = request.getParameter("description");

        if (methodName == null || methodName.trim().isEmpty()) {
        request.getSession().setAttribute("errorMessage", "Method name cannot be empty.");
        response.sendRedirect("PaymentMethodServlet?action=list&showErrorModal=true");
        return;
    }

    // Kiểm tra methodName chỉ chứa số, chữ, dấu - và _
    if (!methodName.matches("^[\\p{L} _-]+$")) {
        request.getSession().setAttribute("errorMessage", 
            "Method name can only contain letters, numbers, -, and _.");
        response.sendRedirect("PaymentMethodServlet?action=list&showErrorModal=true");
        return;
    }

    if (paymentDAO.isPaymentMethodExists(methodName)) {
        request.getSession().setAttribute("errorMessage", "Payment method already exists.");
        response.sendRedirect("PaymentMethodServlet?action=list&showErrorModal=true");
        return;
    }
        
        paymentDAO.addPaymentMethod(methodName, description);
        request.getSession().setAttribute("successMessage", "Payment method added successfully.");
        response.sendRedirect("PaymentMethodServlet?action=list&showSuccessModal=true");
    }

    private void updatePaymentMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("paymentMethodID"));
        String methodName = request.getParameter("methodName");
        String description = request.getParameter("description");

            if (methodName == null || methodName.trim().isEmpty()) {
        request.getSession().setAttribute("errorMessage", "Method name cannot be empty.");
        response.sendRedirect("PaymentMethodServlet?action=list&showErrorModal=true");
        return;
    }

    // Kiểm tra methodName chỉ chứa số, chữ, dấu - và _
    if (!methodName.matches("^[\\p{L} _-]+$")) {
        request.getSession().setAttribute("errorMessage", 
            "Method name can only contain letters, numbers, -, and _.");
        response.sendRedirect("PaymentMethodServlet?action=list&showErrorModal=true");
        return;
    }
        
        paymentDAO.updatePaymentMethod(id, methodName, description);
        request.getSession().setAttribute("successMessage", "Payment method updated successfully.");
        response.sendRedirect("PaymentMethodServlet?action=list&showSuccessModal=true");
    }

    // 5) Xóa mềm
    private void softDeletePaymentMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int paymentMethodID = Integer.parseInt(request.getParameter("paymentMethodID"));
            paymentDAO.softDeletePaymentMethod(paymentMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức này đã được đưa vào thùng rác.");
            response.sendRedirect("PaymentMethodServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Payment method ID.");
            response.sendRedirect("PaymentMethodServlet?action=list");
        }
    }

    // 6) Xóa cứng
    private void hardDeletePaymentMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int paymentMethodID = Integer.parseInt(request.getParameter("paymentMethodID"));
            paymentDAO.hardDeletePaymentMethod(paymentMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức đã bị xóa vĩnh viễn.");
            response.sendRedirect("PaymentMethodServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phương thức không hợp lệ.");
            response.sendRedirect("PaymentMethodServlet?action=listDeleted");
        }
    }

    private void restorePaymentMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int paymentMethodID = Integer.parseInt(request.getParameter("paymentMethodID"));
            paymentDAO.restorePaymentMethod(paymentMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức đã được phục hồi thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phương thức không hợp lệ.");
        }
        response.sendRedirect("PaymentMethodServlet?action=list");
    }
    
    private void searchPaymentMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<Payment> paymentMethods = paymentDAO.searchPaymentMethods(keyword);
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("PaymentMethodManagement.jsp").forward(request, response);
    }
    
}