/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AccountDAO;
import DAO.MyUntilsDAO;
import Model.Account;
import Model.Role;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
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
public class StaffManagementServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "images";
    private static final int ROLE_STAFF = 2;
    private static final String FOLDER = "images";
    private static final MyUntilsDAO myUntilsDAO = new MyUntilsDAO();
    private static int PAGE = 1;
    private static final int PAGE_SIZE = 10;

//    private static final String UPLOAD_DIR = "images";
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
                    case "delete":
                        deleteStaff(request, response);
                        break;
                    case "search":
                        searchStaff(request, response);
                        break;
                    default:
                        showStaff(request, response);
                }
            } else {
                showStaff(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
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
                        addStaff(request, response);
                        break;
                    case "update":
                        updateStaff(request, response);
                        break;
                    default:
                        showStaff(request, response);
                }
            } else {
                showStaff(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        }
    }

    protected void showStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("page") != null) {
                PAGE = Integer.parseInt(request.getParameter("page"));
            }
            int totalPages = myUntilsDAO.getTotalPagesAccount(PAGE_SIZE, ROLE_STAFF);
            List<Account> list = AccountDAO.getAllAccount(ROLE_STAFF, PAGE, PAGE_SIZE);
            List<Role> listRole = AccountDAO.showListRoleTest();
            request.setAttribute("staffs", list);
            request.setAttribute("roles", listRole);
            request.setAttribute("currentPage", PAGE);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("forward", "StaffManagementServlet");
            request.getRequestDispatcher("StaffManagement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error loading staff: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        }
    }

    protected void addStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            String userName = request.getParameter("userName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String address = request.getParameter("address").trim();
            String status = request.getParameter("status").trim();
            int roleID;
            // Validate required fields
            if (userName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                session.setAttribute("errorMessage", "All fields are required.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate email format
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                session.setAttribute("errorMessage", "Invalid email format.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate phone number (assumes 10 digits)
            if (!phoneNumber.matches("\\d{10}")) {
                session.setAttribute("errorMessage", "Phone number must be 10 digits.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Check for existing email or username
            if (AccountDAO.isEmailExists(email)) {
                session.setAttribute("errorMessage", "Email already exists.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            if (password == null || password.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Password cannot be empty.");
                showStaff(request, response);
                return;
            }

            try {
                roleID = Integer.parseInt(request.getParameter("roleID"));
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "Invalid role ID.");
                showStaff(request, response);
                return;
            }
            String passwordHashed = MyUtils.hashPassword(password);
            String oldImage = request.getParameter("currentImage").trim();
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;

            Account a = new Account(roleID, userName, phoneNumber, email, image, passwordHashed, address, status);
            boolean isAdded = AccountDAO.addAdmin(a);
            if (isAdded) {
                request.getSession().setAttribute("successMessage", "Staff added successfully.");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to add staff. Email may already exist.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid input format.");
        } catch (IOException | ServletException e) {
            request.getSession().setAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error adding staff: " + e.getMessage());
        } finally {
            response.sendRedirect("/Admin/StaffManagementServlet");
        }
    }

    protected void updateStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            int accountID;
            try {
                accountID = Integer.parseInt(request.getParameter("accountId"));
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "Invalid account ID.");
                 response.sendRedirect("/Admin/StaffManagementServlet");
                return;
            }
            String userName = request.getParameter("userName").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String email = request.getParameter("email").trim();
            String password = request.getParameter("password").trim();
            String address = request.getParameter("address").trim();
            String status = request.getParameter("status").trim();
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            String oldImage = request.getParameter("currentImage").trim();
            // Validate required fields
            if (userName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                session.setAttribute("errorMessage", "All fields are required.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate email format
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                session.setAttribute("errorMessage", "Invalid email format.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate phone number (assumes 10 digits)
            if (!phoneNumber.matches("\\d{10}")) {
                session.setAttribute("errorMessage", "Phone number must be 10 digits.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Check for existing email or username
            if (AccountDAO.isEmailExists(email)) {
                session.setAttribute("errorMessage", "Email already exists.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Handle password
            String passwordHashed;
            if (!password.isEmpty()) {
                passwordHashed = MyUtils.hashPassword(password);
            } else {
                // Retrieve existing password if not changed
                Account existingAccount = AccountDAO.getInforAccountByID(accountID);
                passwordHashed = existingAccount.getPassword();
            }
            try {
                roleID = Integer.parseInt(request.getParameter("roleID"));
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "Invalid role ID.");
                response.sendRedirect("/Admin/StaffManagementServlet");
                return;
            }

            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;

            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status);
            boolean isUpdated = AccountDAO.updateProfileStaff(a);
            if (isUpdated) {
                request.getSession().setAttribute("successMessage", "Staff updated successfully.");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update staff.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid input format.");
        } catch (IOException | ServletException e) {
            request.getSession().setAttribute("errorMessage", "Error uploading image: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error updating staff: " + e.getMessage());
        } finally {
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        }
    }

    protected void deleteStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("accountId").trim();
            if (idParam == null || idParam.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Account ID is missing.");
                showStaff(request, response);
                return;
            }
            int accountId = Integer.parseInt(idParam.trim());
            boolean isDeleted = AccountDAO.DeleteSoftAccountById(accountId);
            if (isDeleted) {
                request.getSession().setAttribute("successMessage", "Staff deleted successfully.");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete staff.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid account ID format.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error deleting staff: " + e.getMessage());
        } finally {
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        }
    }

    protected void searchStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("search").trim();
            keyword = keyword != null ? keyword : "";
            int page = 1;
            String pageParam = request.getParameter("page").trim();
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam.trim());
                    page = page < 1 ? 1 : page;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            int totalPages = myUntilsDAO.getTotalPagesAccountSearch(PAGE_SIZE, ROLE_STAFF, keyword);
            List<Account> list = AccountDAO.findUser(keyword, ROLE_STAFF, page, PAGE_SIZE);
            List<Role> roleList = AccountDAO.showListRoleTest();
            request.setAttribute("staffs", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("roles", roleList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("action", "search");
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("StaffManagement.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid page number.");
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error searching staff: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
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
