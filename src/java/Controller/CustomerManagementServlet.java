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
//                        addCustomer(request, response);
                        break;
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
        }
    }

    /**
     * Displays the list of customers in the management interface. Retrieves
     * customers with a specific role and the available roles.
     */
    protected void showListCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        try {
            if (request.getParameter("page") != null) {
                PAGE = Integer.parseInt(request.getParameter("page"));
            }
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
        }
    }

    /**
     * Updates an existing customer's information. Handles profile image upload
     * if a new image is provided.
     */
    protected void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
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
                showListCustomer(request, response);
            } else {
                System.out.println("Loi Update");
            }
        } catch (ServletException | IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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
        try {
            String id = request.getParameter("accountId");
            Boolean isDeleted = AccountDAO.DeleteSoftAccountById(Integer.parseInt(id));
            if (isDeleted) {
                showListCustomer(request, response);
            } else {
                System.out.println("loi r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for customers based on a keyword (name, email, etc.). Filters
     * results based on role.
     */
    protected void searchCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("search"); // Lấy từ ô nhập
            if (keyword == null) {
                keyword = ""; // Tránh null gây lỗi
            }
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    PAGE = Integer.parseInt(pageParam.trim()); // Chuyển đổi số nguyên
                    if (PAGE < 1) {
                        PAGE = 1; // Không cho phép số âm
                    }
                } catch (NumberFormatException e) {
                    PAGE = 1; // Nếu lỗi, quay về trang đầu
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
            request.setAttribute("keyword", keyword); // Giữ lại keyword

            request.getRequestDispatcher("CustomerManagement.jsp").forward(request, response);
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
