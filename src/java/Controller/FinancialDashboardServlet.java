/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.FinancialDashboardDAO;
import static DAO.FinancialDashboardDAO.getCategorySales;
import static DAO.FinancialDashboardDAO.getRevenueData;
import Model.CategorySales;
import Model.RevenueData;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class FinancialDashboardServlet extends HttpServlet {

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
            out.println("<title>Servlet FinancialDashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FinancialDashboardServlet at " + request.getContextPath() + "</h1>");
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
        try {
            Gson gson = new Gson();

            request.setAttribute("totalsolds", FinancialDashboardDAO.showTotalSold());
            request.setAttribute("totalrevenues", FinancialDashboardDAO.showTotalRevenue());
            request.setAttribute("totalcustomers", FinancialDashboardDAO.showTotalCustomer());

            // Kiểm tra nếu có tham số filter
            String filter = request.getParameter("filter");

            List<RevenueData> revenueData;
            if ("3months".equals(filter)) {
                revenueData = FinancialDashboardDAO.getLastMonthsRevenue(3);
            } else if ("6months".equals(filter)) {
                revenueData = FinancialDashboardDAO.getLastMonthsRevenue(6);
            } else if ("12months".equals(filter)) {
                revenueData = FinancialDashboardDAO.getLastMonthsRevenue(12);
            } else {
                revenueData = FinancialDashboardDAO.getRevenueData();
            }

            List<CategorySales> categorySales = FinancialDashboardDAO.getCategorySales();
            String categorySalesJson = gson.toJson(categorySales);
            String revenueDataJson = gson.toJson(revenueData);

            request.setAttribute("categorySalesJson", categorySalesJson);
            request.setAttribute("revenueDataJson", revenueDataJson);
            request.setAttribute("currentFilter", filter);

            request.getRequestDispatcher("DashBoard.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
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

    }

    protected void showDashBoard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
