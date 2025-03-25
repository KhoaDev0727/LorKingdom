package Controller;

import DAO.ShippingDAO;
import Model.Shipping;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShippingMethodServlet extends HttpServlet {

    private ShippingDAO shippingDAO = new ShippingDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShippingMethodServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShippingMethodServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Shipping Method Servlet";
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listShippingMethods(request, response);
            } else {
                switch (action) {
                    case "add":
                        addShippingMethod(request, response);
                        break;
                    case "listDeleted":
                        listDeletedShippingMethods(request, response);
                        break;
                    case "update":
                        updateShippingMethod(request, response);
                        break;
                    case "delete":
                        softDeleteShippingMethod(request, response);
                        break;
                    case "hardDelete":
                        hardDeleteShippingMethod(request, response);
                        break;
                    case "restore":
                        restoreShippingMethod(request, response);
                        break;
                    case "search":
                        searchShippingMethods(request, response);
                        break;
                    default:
                        listShippingMethods(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listShippingMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Shipping> shippingMethods = shippingDAO.getAllShippingMethods();
        request.setAttribute("shippingMethods", shippingMethods);
        request.getRequestDispatcher("ShippingMethodManagement.jsp").forward(request, response);
    }

    private void listDeletedShippingMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        ShippingDAO shippingDAO = new ShippingDAO();
        List<Shipping> shippingMethods = shippingDAO.getDeletedShippingMethod();
        request.setAttribute("shippingMethods", shippingMethods);
        request.getRequestDispatcher("ShippingMethodManagement.jsp").forward(request, response);
    }

    private void addShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String methodName = request.getParameter("methodName");
        String description = request.getParameter("description");

        if (methodName == null || methodName.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên phương thức không được để trống.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }

        if (!methodName.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage",
                    "Tên phương thức chỉ có thể chứa chữ cái, số, - và _.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }

        if (shippingDAO.isShippingMethodExists(methodName, 0)) {
            request.getSession().setAttribute("errorMessage", "Phương thức vận chuyển đã tồn tại.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }
        Shipping newShippingMethod = new Shipping(0, methodName, description, 0);
        shippingDAO.addShippingMethod(methodName, description);
        request.getSession().setAttribute("successMessage", "Đã thêm phương thức vận chuyển thành công.");
        response.sendRedirect("ShippingMethodServlet?action=list&showSuccessModal=true");
    }

    private void updateShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("shippingMethodID"));
        String methodName = request.getParameter("methodName");
        String description = request.getParameter("description");

        if (methodName == null || methodName.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên phương thức không được để trống.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }

        if (!methodName.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage",
                    "Tên phương thức chỉ có thể chứa chữ cái, số, - và _.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }

        if (shippingDAO.isShippingMethodExists(methodName, id)) {
            request.getSession().setAttribute("errorMessage", "Phương thức vận chuyển đã tồn tại.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }
        shippingDAO.updateShippingMethod(id, methodName, description);
        request.getSession().setAttribute("successMessage", "Đã cập nhật phương thức vận chuyển thành công.");
        response.sendRedirect("ShippingMethodServlet?action=list&showSuccessModal=true");
    }

    private void softDeleteShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int shippingMethodID = Integer.parseInt(request.getParameter("shippingMethodID"));
            shippingDAO.softDeleteShippingMethod(shippingMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức này đã được đưa vào thùng rác.");
            response.sendRedirect("ShippingMethodServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phương thức vận chuyển không hợp lệ.");
            response.sendRedirect("ShippingMethodServlet?action=list");
        }
    }

    private void hardDeleteShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int shippingMethodID = Integer.parseInt(request.getParameter("shippingMethodID"));
            shippingDAO.hardDeleteShippingMethod(shippingMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức đã bị xóa vĩnh viễn.");
            response.sendRedirect("ShippingMethodServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Phương thức không hợp lệ.");
            response.sendRedirect("ShippingMethodServlet?action=listDeleted");
        }
    }

    private void restoreShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int shippingMethodID = Integer.parseInt(request.getParameter("shippingMethodID"));
            shippingDAO.restoreShippingMethod(shippingMethodID);
            request.getSession().setAttribute("successMessage", "Phương thức đã được phục hồi thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phương thức không hợp lệ.");
        }
        response.sendRedirect("ShippingMethodServlet?action=list");
    }

    private void searchShippingMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<Shipping> shippingMethods = shippingDAO.searchShippingMethods(keyword);
        request.setAttribute("shippingMethods", shippingMethods);
        request.getRequestDispatcher("ShippingMethodManagement.jsp").forward(request, response);
    }
}
