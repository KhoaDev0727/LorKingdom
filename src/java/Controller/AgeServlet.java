/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AgeDAO;
import Model.Age;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author admin1
 */
public class AgeServlet extends HttpServlet {

    AgeDAO ageDAO = new AgeDAO();

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
            out.println("<title>Servlet AgeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AgeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
      
        Integer roleID = (Integer) session.getAttribute("roleID");
        if (roleID == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listAges(request, response);
                    break;
                case "listDeleted":
                    listDeletedAges(request, response);
                    break;
                case "add":
                    addAge(request, response);
                    break;
                case "update":
                    updateAge(request, response);
                    break;
                case "delete":
                    softDeleteAge(request, response);
                    break;
                case "hardDelete":
                    hardDeleteAge(request, response);
                    break;
                case "restore":
                    restoreAge(request, response);
                    break;
                case "search":
                    searchAge(request, response);
                    break;
                default:
                    listAges(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
        }
    }
  // 1) Hiển thị danh sách active
    private void listAges(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Age> ages = ageDAO.getAllAges();
        request.setAttribute("ages", ages);
        request.getRequestDispatcher("AgeManagement.jsp").forward(request, response);
    }

    // 2) Hiển thị danh sách đã xóa mềm
    private void listDeletedAges(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Age> ages = ageDAO.getDeletedAges();
        request.setAttribute("ages", ages);
        request.getRequestDispatcher("AgeManagement.jsp").forward(request, response);
    }

    private void addAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        // Lấy giá trị từ form
        String ageRange = request.getParameter("ageRange");

        // Kiểm tra nếu trống hoặc không hợp lệ (chỉ cho phép số và dấu '-')
        if (ageRange == null || ageRange.trim().isEmpty() || !ageRange.matches("^[\\p{L}\\p{N} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Độ tuổi chỉ được chứa số và dấu gạch ngang (ví dụ: '0-3', '4-6').");
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra độ dài tối đa
        if (ageRange.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Độ tuổi quá dài. Chỉ được phép nhập tối đa 50 ký tự.");
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
            return;
        }

        if (ageDAO.isAgeExists(ageRange)) {
            request.getSession().setAttribute("errorMessage", "Khoản tuổi đã tồn tại.");
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
            return;
        }

        // Nếu hợp lệ, thêm vào cơ sở dữ liệu
        Age age = new Age(0, ageRange, null, 0);
        ageDAO.addAge(age);

        // Đặt thông báo thành công
        request.getSession().setAttribute("successMessage", "Đã thêm độ tuổi thành công.");
        response.sendRedirect("AgeServlet?action=list&showSuccessModal=true");
    }
  private void softDeleteAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int ageID = Integer.parseInt(request.getParameter("ageID"));
            ageDAO.softDeleteAge(ageID);
            request.getSession().setAttribute("successMessage", "Đã xóa độ tuổi thành công.");
            response.sendRedirect("AgeServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Mã độ tuổi không hợp lệ.");
            response.sendRedirect("AgeServlet?action=list");
        }
    }
    private void restoreAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int ageID = Integer.parseInt(request.getParameter("ageID"));
            ageDAO.restoreAge(ageID);
            request.getSession().setAttribute("successMessage", "Đã khôi phục độ tuổi  thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Mã độ tuổi không hợp lệ.");
        }
        response.sendRedirect("AgeServlet?action=list");
    }
    
    // 6) Xóa cứng
    private void hardDeleteAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int ageID = Integer.parseInt(request.getParameter("ageID"));
            ageDAO.hardDeleteAge(ageID);
            request.getSession().setAttribute("successMessage", "Đã xóa độ tuổi thành công.");
            response.sendRedirect("AgeServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Age ID.");
            response.sendRedirect("AgeServlet?action=listDeleted");
        }
    }
    private void searchAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listAges(request, response);
            return;
        }
        List<Age> ages = ageDAO.searchAge(keyword.trim());
        request.setAttribute("ages", ages);
        request.getRequestDispatcher("AgeManagement.jsp").forward(request, response);
    }

    private void updateAge(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int ageID = Integer.parseInt(request.getParameter("ageID"));
            String ageRange = request.getParameter("ageRange");

            if (ageRange == null || ageRange.trim().isEmpty() || !ageRange.matches("^[\\p{L}\\p{N} _-]+$")) {
                request.getSession().setAttribute("errorMessage", "Độ tuổi chỉ được chứa số và dấu gạch ngang (ví dụ: '0-3', '4-6').");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            // Kiểm tra độ dài tối đa
            if (ageRange.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Độ tuổi quá dài. Chỉ được phép nhập tối đa 50 ký tự.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }
            if (ageDAO.isAgeExists(ageRange)) {
                request.getSession().setAttribute("errorMessage", "Khoản tuổi đã tồn tại.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }
            Age age = new Age(0, ageRange, null, 0);
            ageDAO.updateAge(age);
            request.getSession().setAttribute("successMessage", "Đã cập nhật tuổi thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Age ID.");
        }
        response.sendRedirect("AgeServlet?action=list");
    }

  
}
