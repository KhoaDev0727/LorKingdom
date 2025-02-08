/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import Model.Account;
import Model.Role;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
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
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class CustomerManagementServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "images";
    private static final int ROLE_STAFF = 3;

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
                    case "delete":
                        deleteCustomer(request, response);
                        break;
                    case "search":
                        searchCustomer(request, response);
                        break;
                    default:
                        showCustomer(request, response);
                }
            } else {
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
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "add":
                        addCustomer(request, response);
                        break;
                    case "update":
                        updateCustomer(request, response);
                        break;
                    default:
                        showCustomer(request, response);
                }
            } else {
                showCustomer(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int roleId = 3;
        List<Account> listAccount = AccountDAO.showList(roleId);
        List<Role> listRole = AccountDAO.showListRoleTest();
        request.setAttribute("customers", listAccount);
        request.setAttribute("roles", listRole);
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
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            int accountID = Integer.parseInt(request.getParameter("accountId"));
            String userName = request.getParameter("userName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String passwordHashed = hashPassword(request.getParameter("password"));
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            // Lấy ảnh cũ từ request
            String oldImage = request.getParameter("currentImage");
            // Nếu có file mới, upload ảnh mới, ngược lại giữ ảnh cũ
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath)
                    : oldImage;
            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status);
            boolean isUpdate = AccountDAO.updateProfileStaff(a);
            if (isUpdate) {
                showCustomer(request, response);
            } else {
                System.out.println("Loi Update");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("accountId");
            Boolean isDeleted = AccountDAO.DeleteSoftAccountById(Integer.parseInt(id));
            if (isDeleted) {
                showCustomer(request, response);
            } else {
                System.out.println("loi r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void searchCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("search");
            int role = ROLE_STAFF;
            List<Account> list = AccountDAO.searchUser(keyword, role);
            request.setAttribute("customers", list);
            request.getRequestDispatcher("UserManagement.jsp").forward(request, response);
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
