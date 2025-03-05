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
        try {
            // Lấy giá trị từ request
            int ageStart = Integer.parseInt(request.getParameter("ageStart"));
            int ageEnd = Integer.parseInt(request.getParameter("ageEnd"));
            String unit = request.getParameter("unit");

            // Kiểm tra nếu giá trị nhập vào không hợp lệ
            if (ageStart < 0 || ageEnd < 0) {
                request.getSession().setAttribute("errorMessage", "Giá trị không hợp lệ! Tuổi không thể là số âm.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            if (ageStart >= ageEnd) {
                request.getSession().setAttribute("errorMessage", "Giá trị bắt đầu phải nhỏ hơn giá trị kết thúc.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            if (!unit.equals("tháng") && !unit.equals("tuổi")) {
                request.getSession().setAttribute("errorMessage", "Đơn vị không hợp lệ. Chỉ chấp nhận 'tháng' hoặc 'tuổi'.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            // Nếu chọn đơn vị là "tháng", kiểm tra giới hạn tối đa là 24 tháng
            if (unit.equals("tháng")) {
                if (ageEnd > 24) {
                    request.getSession().setAttribute("errorMessage", "Khoảng tuổi theo tháng không thể lớn hơn 24 tháng.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
            }

            // Nếu chọn đơn vị là "tuổi", kiểm tra khoảng cách không quá 5 năm và tối đa 100 tuổi
            if (unit.equals("tuổi")) {
                if ((ageEnd - ageStart) > 5) {
                    request.getSession().setAttribute("errorMessage", "Khoảng cách giữa tuổi bắt đầu và kết thúc không thể lớn hơn 5 năm.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
                if (ageEnd > 28) {
                    request.getSession().setAttribute("errorMessage", "Số tuổi không thể vượt quá 28.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
            }

            String ageRange = ageStart + "-" + ageEnd + " " + unit;

            if (ageDAO.isAgeExists(ageRange)) {
                request.getSession().setAttribute("errorMessage", "Khoảng tuổi đã tồn tại.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            // Nếu hợp lệ, thêm vào cơ sở dữ liệu
            Age age = new Age(0, ageRange, null, 0);
            ageDAO.addAge(age);

            // Đặt thông báo thành công
            request.getSession().setAttribute("successMessage", "Đã thêm độ tuổi thành công.");
            response.sendRedirect("AgeServlet?action=list&showSuccessModal=true");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Giá trị nhập vào không hợp lệ. Vui lòng nhập số nguyên.");
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
        }
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
            int ageStart = Integer.parseInt(request.getParameter("ageStart"));
            int ageEnd = Integer.parseInt(request.getParameter("ageEnd"));
            String unit = request.getParameter("unit");

            // Kiểm tra nếu giá trị nhập vào không hợp lệ
            if (ageStart < 0 || ageEnd < 0) {
                request.getSession().setAttribute("errorMessage", "Giá trị không hợp lệ! Tuổi không thể là số âm.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            if (ageStart >= ageEnd) {
                request.getSession().setAttribute("errorMessage", "Giá trị bắt đầu phải nhỏ hơn giá trị kết thúc.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            if (!unit.equals("tháng") && !unit.equals("tuổi")) {
                request.getSession().setAttribute("errorMessage", "Đơn vị không hợp lệ. Chỉ chấp nhận 'tháng' hoặc 'tuổi'.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            // Nếu chọn đơn vị là "tháng", kiểm tra giới hạn tối đa là 24 tháng
            if (unit.equals("tháng")) {
                if (ageEnd > 24) {
                    request.getSession().setAttribute("errorMessage", "Khoảng tuổi theo tháng không thể lớn hơn 24 tháng.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
            }

            // Nếu chọn đơn vị là "tuổi", kiểm tra khoảng cách không quá 5 năm và tối đa 100 tuổi
            if (unit.equals("tuổi")) {
                if ((ageEnd - ageStart) > 5) {
                    request.getSession().setAttribute("errorMessage", "Khoảng cách giữa tuổi bắt đầu và kết thúc không thể lớn hơn 5 năm.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
                if (ageEnd > 28) {
                    request.getSession().setAttribute("errorMessage", "Số tuổi không thể vượt quá 28.");
                    response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                    return;
                }
            }

            // Gộp lại thành chuỗi: "0-12 tháng" hoặc "1-5 tuổi"
            String ageRange = ageStart + "-" + ageEnd + " " + unit;

            // Kiểm tra xem khoảng tuổi đã tồn tại chưa (ngoại trừ chính nó)
            if (ageDAO.isAgeExists(ageRange)) {
                request.getSession().setAttribute("errorMessage", "Khoảng tuổi đã tồn tại.");
                response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
                return;
            }

            // Nếu hợp lệ, cập nhật vào cơ sở dữ liệu
            Age age = new Age(ageID, ageRange, null, 0);
            ageDAO.updateAge(age);

            // Đặt thông báo thành công
            request.getSession().setAttribute("successMessage", "Đã cập nhật độ tuổi thành công.");
            response.sendRedirect("AgeServlet?action=list&showSuccessModal=true");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Giá trị nhập vào không hợp lệ. Vui lòng nhập số nguyên.");
            response.sendRedirect("AgeServlet?action=list&showErrorModal=true");
        }
    }

}
