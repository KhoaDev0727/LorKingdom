/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.SexDAO;
import Model.Sex;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class SexServlet extends HttpServlet {

    SexDAO sexDAO = new SexDAO();

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
            out.println("<title>Servlet SexServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SexServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

    /**
     * Xử lý yêu cầu dựa trên tham số action
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listSexes(request, response);
            } else {
                switch (action) {
                    case "add":
                        addSex(request, response);
                        break;
                    case "delete":
                        deleteSex(request, response);
                        break;
                    case "update":
                        updateSex(request, response);
                        break;
                    case "search":
                        searchSex(request, response);
                        break;
                    default:
                        listSexes(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Liệt kê tất cả các giới tính
     */
    private void listSexes(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Sex> sexes = sexDAO.getAllSexes();
        request.setAttribute("sexes", sexes);
        request.getRequestDispatcher("SexManagement.jsp").forward(request, response);
    }

    /**
     * Thêm một giới tính mới
     */
    private void addSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");

        // Kiểm tra dữ liệu: tên không được để trống và độ dài tối đa 50 ký tự
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên giới tính không được để trống.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Tên quá dài. Tối đa 50 ký tự cho phép.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        // Kiểm tra xem giới tính đã tồn tại chưa (nếu cần)
        if (sexDAO.isSexExists(name)) {
            request.getSession().setAttribute("errorMessage", "Giới tính đã tồn tại.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        // Tạo đối tượng Sex (ID=0 và CreatedAt=null, DB sẽ tự set giá trị mặc định nếu có)
        Sex sex = new Sex(0, name.trim(), null);
        sexDAO.addSex(sex);

        request.getSession().setAttribute("successMessage", "Giới tính được thêm thành công.");
        response.sendRedirect("SexServlet?action=list&showSuccessModal=true");
    }

    /**
     * Cập nhật thông tin giới tính
     */
    private void updateSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int sexID = Integer.parseInt(request.getParameter("sexID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên giới tính không được để trống.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }
            if (name.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Tên quá dài. Tối đa 50 ký tự cho phép.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }

            Sex sex = new Sex(sexID, name.trim(), null);
            sexDAO.updateSex(sex);
            request.getSession().setAttribute("successMessage", "Giới tính được cập nhật thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("SexServlet?action=list");
    }

    /**
     * Xóa giới tính theo ID
     */
    private void deleteSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int sexID = Integer.parseInt(request.getParameter("sexID"));
            sexDAO.deleteSex(sexID);
            request.getSession().setAttribute("successMessage", "Giới tính được xóa thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("SexServlet?action=list");
    }

    /**
     * Tìm kiếm giới tính theo tên (search theo keyword)
     */
    private void searchSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listSexes(request, response);
            return;
        }
        List<Sex> allSexes = sexDAO.getAllSexes();
        List<Sex> filteredSexes = new ArrayList<>();
        for (Sex s : allSexes) {
            if (s.getName() != null && s.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                filteredSexes.add(s);
            }
        }
        request.setAttribute("sexes", filteredSexes);
        request.getRequestDispatcher("SexManagement.jsp").forward(request, response);
    }
}
