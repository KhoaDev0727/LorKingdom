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
    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_WAREHOUSE = 4;
    private static final String FOLDER = "images";
    private static final MyUntilsDAO myUntilsDAO = new MyUntilsDAO();
    private static int PAGE = 1;
    private static final int PAGE_SIZE = 10;

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
        HttpSession session = request.getSession();
        try {
            // Kiểm tra đăng nhập (ở đây giả sử roleID của người dùng được lưu trong session)
            Integer userRoleID = (Integer) session.getAttribute("roleID");
            if (userRoleID == null) {
                response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp");
                return;
            }
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
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi. " + e.getMessage());
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
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi. " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");

        }
    }

    protected void showStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("page") != null) {
                PAGE = Integer.parseInt(request.getParameter("page"));
            }
            int totalPages = myUntilsDAO.getTotalPagesAccountStaff(PAGE_SIZE, ROLE_STAFF, ROLE_ADMIN, ROLE_WAREHOUSE);
            List<Account> list = AccountDAO.getAllAccountStaff(ROLE_STAFF, ROLE_ADMIN, ROLE_WAREHOUSE, PAGE, PAGE_SIZE);
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
            String userName = request.getParameter("userName").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String email = request.getParameter("email").trim();
            String password = request.getParameter("password").trim();
            String address = request.getParameter("address").trim();
            String status = request.getParameter("status");
            // Handle password
            String passwordHashed = "";
            int roleID;
            // Validate required fields
            if (userName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                session.setAttribute("errorMessage", "Tất cả các trường là bắt buộc.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate email format
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                session.setAttribute("errorMessage", "Định dạng email không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate phone number (assumes 10 digits)
            if (!phoneNumber.matches("\\d{10}")) {
                session.setAttribute("errorMessage", "Số điện thoại phải có 10 chữ số.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Check for existing email or username
            if (AccountDAO.isEmailExists(email)) {
                session.setAttribute("errorMessage", "Email đã tồn tại.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            if (password == null || password.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Mật khẩu không được để trống.");
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

            if (!password.isEmpty()) {
                passwordHashed = MyUtils.hashPassword(password);
            }
            System.out.println(userName + "  " + phoneNumber + " " + email + " " + passwordHashed + " " + address + " " + status);
            // Lấy ảnh cũ từ request
            String oldImage = request.getParameter("currentImage");
            // Nếu có file mới, upload ảnh mới, ngược lại giữ ảnh cũ
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;
            Account a = new Account(roleID, userName, phoneNumber, email, image, passwordHashed, address, status);
            boolean isUpdate = AccountDAO.addAdmin(a);
            if (isUpdate) {
                request.getSession().setAttribute("successMessage", "Thêm nhân viên thành công.");
            } else {
                request.getSession().setAttribute("errorMessage", "Thêm nhân viên thất bại. Email có thể đã tồn tại.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Định dạng đầu vào không hợp lệ.");
        } catch (IOException | ServletException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi tải lên hình ảnh: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi thêm nhân viên: " + e.getMessage());
        } finally {
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
            return;
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
                request.getSession().setAttribute("errorMessage", "ID tài khoản không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
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
                session.setAttribute("errorMessage", "Tất cả các trường là bắt buộc.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate email format
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                session.setAttribute("errorMessage", "Định dạng email không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Validate phone number (assumes 10 digits)
            if (!phoneNumber.matches("\\d{10}")) {
                session.setAttribute("errorMessage", "Số điện thoại phải có 10 chữ số.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Check for existing email or username
            if (AccountDAO.isEmailExistsUpdate(email)) {
                session.setAttribute("errorMessage", "Email đã tồn tại.");
                response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
                return;
            }
            // Handle password
            String passwordHashed;
            Account existingAccount = AccountDAO.getInforAccountByID(accountID);
            if (!password.equals(existingAccount.getPassword())) {
                passwordHashed = MyUtils.hashPassword(password);
            } else {
                passwordHashed = existingAccount.getPassword();
            }
            // Nếu có file mới, upload ảnh mới, ngược lại giữ ảnh cũ
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;
            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status);
            boolean isUpdate = AccountDAO.updateProfileStaff(a);
            if (isUpdate) {
                request.getSession().setAttribute("successMessage", "Cập nhật nhân viên thành công.");
                showStaff(request, response);
            } else {
                request.getSession().setAttribute("errorMessage", "Cập nhật nhân viên thất bại.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Định dạng đầu vào không hợp lệ.");
        } catch (IOException | ServletException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi tải lên hình ảnh: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi cập nhật nhân viên.: " + e.getMessage());
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
            Boolean isDeleted = AccountDAO.DeleteSoftAccountById(Integer.parseInt(idParam));
            if (isDeleted) {
                request.getSession().setAttribute("successMessage", "Xóa nhân viên thành công.");
            } else {
                request.getSession().setAttribute("errorMessage", "Xóa nhân viên thất bại.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Định dạng ID tài khoản không hợp lệ.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi xóa nhân viên: " + e.getMessage());
        } finally {
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        }
    }

    protected void searchStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("search").trim(); // Lấy từ ô nhập
            if (keyword == null) {
                keyword = ""; // Tránh null gây lỗi
            }
            String pageParam = request.getParameter("page");
            int page = 1; // Mặc định là trang 1 nếu không có tham số
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam.trim()); // Chuyển đổi số nguyên
                    if (page < 1) {
                        page = 1; // Không cho phép số âm
                    }
                } catch (NumberFormatException e) {
                    page = 1; // Nếu lỗi, quay về trang đầu
                }
            }

            int totalPages = myUntilsDAO.getTotalPagesAccountSearchStaff(PAGE_SIZE, ROLE_ADMIN, ROLE_STAFF, ROLE_WAREHOUSE, keyword);
            List<Account> list = AccountDAO.findUser(keyword, ROLE_STAFF, page, PAGE_SIZE);
            List<Role> roleList = AccountDAO.showListRoleTest();
            request.setAttribute("staffs", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("roles", roleList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("action", "search");
            request.setAttribute("keyword", keyword); // Giữ lại keyword

            request.getRequestDispatcher("StaffManagement.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Số trang không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/Admin/StaffManagementServlet");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi tìm kiếm nhân viên: " + e.getMessage());
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
