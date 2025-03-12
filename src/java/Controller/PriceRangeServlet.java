/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.PriceRangeDAO;
import Model.PriceRange;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author admin1
 */
public class PriceRangeServlet extends HttpServlet {

    PriceRangeDAO priceRangeDAO = new PriceRangeDAO();

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
            out.println("<title>Servlet PriceRangeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PriceRangeServlet at " + request.getContextPath() + "</h1>");
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        // Kiểm tra đăng nhập (nếu cần)
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
                    listPriceRanges(request, response);
                    break;
                case "listDeleted":
                    listDeletedPriceRanges(request, response);
                    break;
                case "add":
                    addPriceRange(request, response);
                    break;
                case "update":
                    updatePriceRange(request, response);
                    break;
                case "delete":
                    softDeletePriceRange(request, response);
                    break;
                case "hardDelete":
                    hardDeletePriceRange(request, response);
                    break;
                case "restore":
                    restorePriceRange(request, response);
                    break;
                case "search":
                    searchPriceRange(request, response);
                    break;
                default:
                    listPriceRanges(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
        }
    }

    private void listPriceRanges(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<PriceRange> list = priceRangeDAO.getAllActivePriceRanges();
        request.setAttribute("priceRanges", list);
        request.getRequestDispatcher("PriceRangeManagement.jsp").forward(request, response);
    }

    private void listDeletedPriceRanges(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<PriceRange> list = priceRangeDAO.getDeletedPriceRanges();
        request.setAttribute("priceRanges", list);
        request.getRequestDispatcher("PriceRangeManagement.jsp").forward(request, response);
    }

    private void addPriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String priceStartStr = request.getParameter("priceStart");
            String priceEndStr = request.getParameter("priceEnd");

            if (priceStartStr == null || priceEndStr == null
                    || priceStartStr.trim().isEmpty() || priceEndStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ các trường.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            String cleanedPriceStart = priceStartStr.replaceAll("[^\\d]", "");
            String cleanedPriceEnd = priceEndStr.replaceAll("[^\\d]", "");
            if (cleanedPriceStart.length() < 4 || cleanedPriceEnd.length() < 4) {
                session.setAttribute("errorMessage", "Giá trị phải có ít nhất 4 chữ số.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            // Xử lý đầu vào nếu cần (loại bỏ dấu phân cách nếu có)
            priceStartStr = priceStartStr.replace(".", "").replace(",", ".");
            priceEndStr = priceEndStr.replace(".", "").replace(",", ".");

            double priceStart = Double.parseDouble(priceStartStr);
            double priceEnd = Double.parseDouble(priceEndStr);

            if (priceStart < 0) {
                session.setAttribute("errorMessage", "Giá bắt đầu không được nhỏ hơn 0.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceEnd > 50000000) {
                session.setAttribute("errorMessage", "Giá kết thúc không được lớn hơn 50,000,000.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceEnd < priceStart) {
                session.setAttribute("errorMessage", "Giá kết thúc không được nhỏ hơn giá bắt đầu.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }

            if (priceStart == priceEnd) {
                session.setAttribute("errorMessage", "Giá bắt đầu và giá kết thúc không được bằng nhau.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceStart != Math.floor(priceStart) || priceEnd != Math.floor(priceEnd)) {
                session.setAttribute("errorMessage", "Giá trị không được có phần thập phân.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPriceStart = formatter.format(priceStart);
            String formattedPriceEnd = formatter.format(priceEnd);

            String priceRangeStr = formattedPriceStart + " - " + formattedPriceEnd;
            if (priceRangeDAO.isPriceRangeExists(priceRangeStr)) {
                session.setAttribute("errorMessage", "Khoảng giá đã tồn tại.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }

            PriceRange priceRange = new PriceRange(0, priceRangeStr.trim(), null, 0);
            priceRangeDAO.addPriceRange(priceRange);

            session.setAttribute("successMessage", "Khoảng giá được thêm thành công.");
            response.sendRedirect("PriceRangeServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Giá trị nhập không hợp lệ.");
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
        }
    }

    private void updatePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int id = Integer.parseInt(request.getParameter("priceRangeID"));
            String priceStartStr = request.getParameter("priceStart");
            String priceEndStr = request.getParameter("priceEnd");

            if (priceStartStr == null || priceEndStr == null
                    || priceStartStr.trim().isEmpty() || priceEndStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ các trường.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            String cleanedPriceStart = priceStartStr.replaceAll("[^\\d]", "");
            String cleanedPriceEnd = priceEndStr.replaceAll("[^\\d]", "");
            if (cleanedPriceStart.length() < 4 || cleanedPriceEnd.length() < 4) {
                session.setAttribute("errorMessage", "Giá trị phải có ít nhất 4 chữ số.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            // Xử lý đầu vào nếu cần (loại bỏ dấu phân cách nếu có)
            priceStartStr = priceStartStr.replace(".", "").replace(",", ".");
            priceEndStr = priceEndStr.replace(".", "").replace(",", ".");

            double priceStart = Double.parseDouble(priceStartStr);
            double priceEnd = Double.parseDouble(priceEndStr);

            if (priceStart < 0) {
                session.setAttribute("errorMessage", "Giá bắt đầu không được nhỏ hơn 0.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceEnd > 50000000) {
                session.setAttribute("errorMessage", "Giá kết thúc không được lớn hơn 50,000,000.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceEnd < priceStart) {
                session.setAttribute("errorMessage", "Giá kết thúc không được nhỏ hơn giá bắt đầu.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }

            if (priceStart == priceEnd) {
                session.setAttribute("errorMessage", "Giá bắt đầu và giá kết thúc không được bằng nhau.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            if (priceStart != Math.floor(priceStart) || priceEnd != Math.floor(priceEnd)) {
                session.setAttribute("errorMessage", "Giá trị không được có phần thập phân.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPriceStart = formatter.format(priceStart);
            String formattedPriceEnd = formatter.format(priceEnd);

            String priceRangeStr = formattedPriceStart + " - " + formattedPriceEnd;
            if (priceRangeDAO.isPriceRangeExists(priceRangeStr)) {
                session.setAttribute("errorMessage", "Khoảng giá đã tồn tại.");
                response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
                return;
            }

            PriceRange priceRange = new PriceRange(id, priceRangeStr.trim(), null, 0);
            priceRangeDAO.updatePriceRange(priceRange);
            session.setAttribute("successMessage", "Cập nhật khoảng giá thành công.");
            response.sendRedirect("PriceRangeServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Giá trị nhập không hợp lệ.");
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
        }
    }

    private void softDeletePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int prID = Integer.parseInt(request.getParameter("priceRangeID"));
            priceRangeDAO.softDeletePriceRange(prID);
            request.getSession().setAttribute("successMessage", "Đã xóa mềm Price Range thành công.");
            response.sendRedirect("PriceRangeServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phạm vi giá không hợp lệ.");
            response.sendRedirect("PriceRangeServlet?action=list");
        }
    }

    private void hardDeletePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int prID = Integer.parseInt(request.getParameter("priceRangeID"));
            priceRangeDAO.hardDeletePriceRange(prID);
            request.getSession().setAttribute("successMessage", "Đã xóa cứng phạm vi giá  thành công.");
            response.sendRedirect("PriceRangeServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phạm vi giá không hợp lệ.");
            response.sendRedirect("PriceRangeServlet?action=listDeleted");
        }
    }

    private void restorePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int prID = Integer.parseInt(request.getParameter("priceRangeID"));
            priceRangeDAO.restorePriceRange(prID);
            request.getSession().setAttribute("successMessage", "Đã khôi phục Price Range thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID phạm vi giá không hợp lệ.");
        }
        response.sendRedirect("PriceRangeServlet?action=list");
    }

    private void searchPriceRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<PriceRange> priceRanges;
        if (keyword == null || keyword.trim().isEmpty()) {
            priceRanges = priceRangeDAO.getAllActivePriceRanges();
        } else {
            priceRanges = priceRangeDAO.searchPriceRange(keyword.trim());
        }
        request.setAttribute("priceRanges", priceRanges);
        request.getRequestDispatcher("PriceRangeManagement.jsp").forward(request, response);
    }

}
