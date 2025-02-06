/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class StaffManagementServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StaffManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffManagementServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "add":
                        addStaff(request, response);
                        break;
                    case "update":
                        updateStaff(request, response);
                        break;
                    case "delete":
                        deleteStaff(request, response);
                        break;
                    default:
                        showStaff(request, response);
                }
            } else {
                showStaff(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void showStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int roleId = 2;
            List<Account> list = AccountDAO.showList(roleId);
            for (Account account : list) {
                System.out.println(account.getAccountId());
            }
            request.setAttribute("staffs", list);
            request.getRequestDispatcher("StaffManagement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    private static String hashPassword(String password) {

        try {
            // Use SHA-256 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Get bytes from the password string using UTF-8 encoding
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            // Return the hashed password as a hexadecimal string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle error if the hashing algorithm is not found
            throw new RuntimeException("Error hashing password", e);
        }
    }

    protected void updateStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int accountID = Integer.parseInt(request.getParameter("accountId"));
            String userName = request.getParameter("userName");
            String image = request.getParameter("image");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String passwordHashed = hashPassword(request.getParameter("password"));
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));

            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status, Timestamp.from(Instant.now()));
            boolean isUpdate = AccountDAO.updateProfile(a);
            if (isUpdate) {
                showStaff(request, response);
            } else {
                System.out.println("Loi Update");
            }
        } catch (Exception e) {
        }
    }

    protected void deleteStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("accountId");
            Boolean isDeleted = AccountDAO.DeleteSoftAccountById(Integer.parseInt(id));
            if (isDeleted) {
                showStaff(request, response);
            } else {
                System.out.println("loi r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
