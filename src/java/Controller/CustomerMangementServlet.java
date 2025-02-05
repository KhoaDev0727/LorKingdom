/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CustomerDAO;
import Model.Account;
import java.io.IOException;
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
public class CustomerMangementServlet extends HttpServlet {

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
            System.out.println(action);
            switch (action) {
                case "add":
                    addCustomer(request, response);
                    break;
                case "update":
                    updateCustomer(request, response);
                    break;
                case "delete":
                    deleteCustomer(request, response);
                    break;
                default:
                    showCustomer(request, response);
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

    protected void showCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Account> list = CustomerDAO.showList();
        request.setAttribute("customers", list);
        request.getRequestDispatcher("UserManagement.jsp").forward(request, response);
    }

    protected void addCustomer(HttpServletRequest request, HttpServletResponse response)
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

    protected void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int accountID = Integer.parseInt(request.getParameter("accountId"));
            String userName = request.getParameter("userName");
            String image = request.getParameter("image");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String passwordHashed = hashPassword(request.getParameter("password"));
            String address = request.getParameter("address");
            double balance = Double.parseDouble(request.getParameter("balance"));
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));

            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status, balance, Timestamp.from(Instant.now()));
            boolean isUpdate = CustomerDAO.updateProfile(a);
            if (isUpdate) {
                showCustomer(request, response);
            }else{
                System.out.println("Loi Update");
            }
        } catch (Exception e) {
        }
    }

    protected void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("accountId");
            Boolean isDeleted = CustomerDAO.DeleteSoftAccountById(Integer.parseInt(id));
            if (isDeleted) {
                showCustomer(request, response);
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
