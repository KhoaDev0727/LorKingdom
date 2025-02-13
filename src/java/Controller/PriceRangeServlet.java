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
import java.sql.SQLException;
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
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listPriceRanges(request, response);
            } else {
                switch (action) {
                    case "add":
                        addPriceRange(request, response);
                        break;
                    case "delete":
                        deletePriceRange(request, response);
                        break;
                    case "update":
                        updatePriceRange(request, response);
                        break;
                    default:
                        listPriceRanges(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listPriceRanges(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<PriceRange> priceRanges = priceRangeDAO.getAllPriceRanges();
        request.setAttribute("priceRanges", priceRanges);
        request.getRequestDispatcher("PriceRangeManagement.jsp").forward(request, response);
    }

    private void addPriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("priceRange");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Khoảng giá không được để trống.");
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
            return;
        }
        if (name.length() > 100) {
            request.getSession().setAttribute("errorMessage", "Tên quá dài. Tối đa 100 ký tự.");
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
            return;
        }

        if (priceRangeDAO.isPriceRangeExists(name)) {
            request.getSession().setAttribute("errorMessage", "Khoảng giá đã tồn tại.");
            response.sendRedirect("PriceRangeServlet?action=list&showErrorModal=true");
            return;
        }

        PriceRange priceRange = new PriceRange(0, name.trim(), null);
        priceRangeDAO.addPriceRange(priceRange);

        request.getSession().setAttribute("successMessage", "Khoảng giá được thêm thành công.");
        response.sendRedirect("PriceRangeServlet?action=list&showSuccessModal=true");
    }

    private void updatePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("priceRangeID"));
        String name = request.getParameter("priceRange");

        PriceRange priceRange = new PriceRange(id, name.trim(), null);
        priceRangeDAO.updatePriceRange(priceRange);
        response.sendRedirect("PriceRangeServlet?action=list");
    }

    private void deletePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("priceRangeID"));
        priceRangeDAO.deletePriceRange(id);
        response.sendRedirect("PriceRangeServlet?action=list");
    }
}
