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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
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
    private static final String FOLDER = "images";
    private static final int ROLE_CUSTOMER = 3;
    private static final MyUntilsDAO myUntilsDAO = new MyUntilsDAO();
    private static int PAGE = 1;
    private static final int PAGE_SIZE = 10;

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    /*
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
                        showListCustomer(request, response);
                }
            } else {
                showListCustomer(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
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
                    case "update":
                        updateCustomer(request, response);
                        break;
                    default:
                        showListCustomer(request, response);
                }
            } else {
                showListCustomer(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
        }
    }

    /**
     * Displays the list of customers in the management interface. Retrieves
     * customers with a specific role and the available roles.
     */
    protected void showListCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    PAGE = Integer.parseInt(pageParam.trim());
                    if (PAGE < 1) {
                        PAGE = 1; // Ensure page is positive
                    }
                } catch (NumberFormatException e) {
                    PAGE = 1; // Default to page 1 for invalid input
                }
            }
            HttpSession session = request.getSession();
            int totalPages = myUntilsDAO.getTotalPagesAccount(PAGE_SIZE, ROLE_CUSTOMER);
            List<Account> listAccount = AccountDAO.getAllAccount(ROLE_CUSTOMER, PAGE, PAGE_SIZE);
            List<Role> listRole = AccountDAO.showListRoleTest();
            request.setAttribute("roles", listRole);
            request.setAttribute("customers", listAccount);
            request.setAttribute("currentPage", PAGE);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("forward", "CustomerManagementServlet");
            request.getRequestDispatcher("CustomerManagement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Lỗi tải khách hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
        }
    }

    /**
     * Updates an existing customer's information. Handles profile image upload
     * if a new image is provided.
     */
    protected void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        HttpSession session = request.getSession();
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            // Retrieve parameters
            int accountID = Integer.parseInt(request.getParameter("accountId"));
            String userName = request.getParameter("userName").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String email = request.getParameter("email").trim();
            String password = request.getParameter("password").trim();
            String address = request.getParameter("address").trim();
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            String oldImage = request.getParameter("currentImage");
            // Validate required fields
            if (userName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                session.setAttribute("errorMessage", "Tất cả các trường đều bắt buộc.");
                response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
                return;
            }
            // Validate email format
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                session.setAttribute("errorMessage", "Định dạng email không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
                return;
            }
            // Validate phone number (assumes 10 digits)
            if (!phoneNumber.matches("\\d{10}")) {
                session.setAttribute("errorMessage", "Số điện thoại phải có 10 chữ số.");
                response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
                return;
            }
            // Check for existing email or username
            if (AccountDAO.isEmailExists(email)) {
                session.setAttribute("errorMessage", "Email đã tồn tại.");
                response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
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
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;
            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status);
            boolean isUpdate = AccountDAO.updateProfileStaff(a);
            if (isUpdate) {
                request.getSession().setAttribute("successMessage", "Cập nhật khách hàng thành công..");
            } else {
                request.getSession().setAttribute("errorMessage", "Cập nhật khách hàng thất bại.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Định dạng đầu vào không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Lỗi khi cập nhật khách hàng:" + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/Admin/CustomerManagementServlet");
    }

    /**
     * Deletes a customer using soft delete (logical deletion).Instead of
     * permanently removing the account, it changes the status.
     *
     * @param request
     * @param response
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        try {
            String id = request.getParameter("accountId");
            if (id == null || id.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Mã khách hàng là bắt buộc");
                response.sendRedirect("/Admin/CustomerManagementServlet");
                return;
            }
            Boolean isDeleted = AccountDAO.DeleteSoftAccountById(Integer.parseInt(id));
            if (isDeleted) {
                session.setAttribute("successMessage", "Xóa khách hàng thành công.");
            } else {
                session.setAttribute("errorMessage", "Xóa khách hàng thất bại.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Định dạng mã khách hàng không hợp lệ.");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi xóa: " + e.getMessage());
        }
        response.sendRedirect("/Admin/CustomerManagementServlet");
    }

    /**
     * Searches for customers based on a keyword (name, email, etc.). Filters
     * results based on role.
     */
    protected void searchCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int PAGE = 1;
        try {
            String keyword = request.getParameter("search");
            if (keyword == null) {
                keyword = "";
            }
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    PAGE = Integer.parseInt(pageParam.trim());
                    if (PAGE < 1) {
                        PAGE = 1;
                    }
                } catch (NumberFormatException e) {
                    PAGE = 1;
                }
            }
            int totalPages = myUntilsDAO.getTotalPagesAccountSearch(PAGE_SIZE, ROLE_CUSTOMER, keyword);
            List<Account> list = AccountDAO.findUser(keyword, ROLE_CUSTOMER, PAGE, PAGE_SIZE);
            List<Role> roleList = AccountDAO.showListRoleTest();
            request.setAttribute("customers", list);
            request.setAttribute("currentPage", PAGE);
            request.setAttribute("roles", roleList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("action", "search");
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("CustomerManagement.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi tìm kiếm: " + e.getMessage());
            response.sendRedirect("/Admin/CustomerManagementServlet");
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
