package Controller;

import DBConnect.DBConnection;
import Model.Shipping;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/Admin/ShippingMethodServlet")
public class ShippingMethodServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DBConnection.getConnection()) {
            if ("add".equals(action)) {
                addShippingMethod(conn, request);
            } else if ("list".equals(action)) {
                listShippingMethods(conn, request, null);
            } else if ("update".equals(action)) {
                updateShippingMethod(conn, request);
            } else if ("delete".equals(action)) {
                deleteShippingMethod(conn, request);
            } else if ("search".equals(action)) {
                String keyword = request.getParameter("search");
                listShippingMethods(conn, request, keyword);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.getRequestDispatcher("/Admin/ShippingMethodManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShippingMethodServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void addShippingMethod(Connection conn, HttpServletRequest request) throws SQLException {
        String methodName = request.getParameter("methodName");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        String description = request.getParameter("description");

        String sql = "INSERT INTO ShippingMethod (MethodName, Price, Description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, methodName);
            stmt.setBigDecimal(2, price);
            stmt.setString(3, description);
            stmt.executeUpdate();
        }
        listShippingMethods(conn, request, null);
    }

   private void listShippingMethods(Connection conn, HttpServletRequest request, String keyword) throws SQLException {
    List<Shipping> shippingMethods = new ArrayList<>();
    String sql = "SELECT * FROM ShippingMethod";

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql += " WHERE LOWER(MethodName) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?)";
    }

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
        }
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                shippingMethods.add(new Shipping(
                    rs.getInt("ShippingMethodID"),
                    rs.getString("MethodName"),
                    rs.getBigDecimal("Price"),
                    rs.getString("Description")
                ));
            }
        }
    }

    if (shippingMethods.isEmpty() && keyword != null) {
        request.setAttribute("noResults", true);
    }

    request.setAttribute("shippingMethods", shippingMethods);
    
}

    private void updateShippingMethod(Connection conn, HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("shippingMethodID"));
        String methodName = request.getParameter("methodName");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        String description = request.getParameter("description");

        String sql = "UPDATE ShippingMethod SET MethodName = ?, Price = ?, Description = ? WHERE ShippingMethodID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, methodName);
            stmt.setBigDecimal(2, price);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
        listShippingMethods(conn, request, null);
    }

    private void deleteShippingMethod(Connection conn, HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("shippingMethodID"));
        String sql = "DELETE FROM ShippingMethod WHERE ShippingMethodID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        listShippingMethods(conn, request, null);
    }
}
