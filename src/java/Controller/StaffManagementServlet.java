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
        }
    }

    protected void addStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            String userName = request.getParameter("userName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String passwordHashed = MyUtils.hashPassword(request.getParameter("password"));
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));
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
                showStaff(request, response);
            } else {
                System.out.println("Loi Update");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String uploadPath = request.getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
            int accountID = Integer.parseInt(request.getParameter("accountId"));
            String userName = request.getParameter("userName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String passwordHashed = MyUtils.hashPassword(request.getParameter("password"));
            String address = request.getParameter("address");
            String status = request.getParameter("status");
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            // Lấy ảnh cũ từ request
            String oldImage = request.getParameter("currentImage");
            // Nếu có file mới, upload ảnh mới, ngược lại giữ ảnh cũ
            Part filePart = request.getPart("image");
            String image = (filePart.getSize() > 0)
                    ? UploadImage.uploadFile(filePart, uploadPath, FOLDER)
                    : oldImage;
            Account a = new Account(accountID, roleID, userName, phoneNumber, image, email, passwordHashed, address, status);
            boolean isUpdate = AccountDAO.updateProfileStaff(a);
            if (isUpdate) {
                showStaff(request, response);
            } else {
                System.out.println("Loi Update");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    protected void searchStaff(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("search"); // Lấy từ ô nhập
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

            int totalPages = myUntilsDAO.getTotalPagesAccountSearch(PAGE_SIZE, ROLE_STAFF, keyword);
            List<Account> list = AccountDAO.findUser(keyword, ROLE_STAFF, page, PAGE_SIZE);
            List<Role> roleList = AccountDAO.showListRoleTest();
            request.setAttribute("staffs", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("roles", roleList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("action", "search");
            request.setAttribute("keyword", keyword); // Giữ lại keyword

            request.getRequestDispatcher("StaffManagement.jsp").forward(request, response);
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
