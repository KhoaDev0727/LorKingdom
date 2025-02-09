package Controller;

import DAO.ShippingDAO;
import Model.Shipping;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
        try (PrintWriter out = response.getWriter()) {
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
                    case "update":
                        updateShippingMethod(request, response);
                        break;
                    case "delete":
                        deleteShippingMethod(request, response);
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
            request.getRequestDispatcher("Admin/error.jsp").forward(request, response);
        }
    }

    private void listShippingMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Shipping> shippingMethods = shippingDAO.getAllShippingMethods();
        request.setAttribute("shippingMethods", shippingMethods);
        request.getRequestDispatcher("ShippingMethodManagement.jsp").forward(request, response);
    }

    private void addShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String methodName = request.getParameter("methodName");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        String description = request.getParameter("description");

        if (shippingDAO.isShippingMethodExists(methodName)) {
            request.getSession().setAttribute("errorMessage", "Shipping method already exists.");
            response.sendRedirect("ShippingMethodServlet?action=list&showErrorModal=true");
            return;
        }

        Shipping newShippingMethod = new Shipping(0, methodName, price, description);
        shippingDAO.addShippingMethod(methodName, price, description);
        request.getSession().setAttribute("successMessage", "Shipping method added successfully.");
        response.sendRedirect("ShippingMethodServlet?action=list&showSuccessModal=true");
    }

    private void updateShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("shippingMethodID"));
        String methodName = request.getParameter("methodName");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        String description = request.getParameter("description");

        shippingDAO.updateShippingMethod(id, methodName, price, description);
        request.getSession().setAttribute("successMessage", "Shipping method updated successfully.");
        response.sendRedirect("ShippingMethodServlet?action=list&showSuccessModal=true");
    }

    private void deleteShippingMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("shippingMethodID"));
        shippingDAO.deleteShippingMethod(id);
        request.getSession().setAttribute("successMessage", "Shipping method deleted successfully.");
        response.sendRedirect("ShippingMethodServlet?action=list&showSuccessModal=true");
    }

    private void searchShippingMethods(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<Shipping> shippingMethods = shippingDAO.searchShippingMethods(keyword);
        request.setAttribute("shippingMethods", shippingMethods);
        request.getRequestDispatcher("ShippingMethodManagement.jsp").forward(request, response);
    }
}
