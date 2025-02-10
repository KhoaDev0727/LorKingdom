/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.PriceRangeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import Model.PriceRange;

/**
 *
 * @author Truong Van Khang - CE181852
 */
@WebServlet(name = "PriceRangeManagementServlet", urlPatterns = {"/Admin/PriceRangeManagementServlet"})
public class PriceRangeManagementServlet extends HttpServlet {

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
            out.println("<title>Servlet PriceRangeManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PriceRangeManagementServlet at " + request.getContextPath() + "</h1>");
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
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "delete":
                        deletePriceRange(request, response);
                        break;
                    case "search":
                        searchPriceRange(request, response);
                        break;
                    default:
                        showPriceRange(request, response);
                }
            } else {
                showPriceRange(request, response);
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
                        addPriceRange(request, response);
                        break;
                    case "update":
                        updatePriceRange(request, response);
                        break;
                    default:
                        showPriceRange(request, response);
                }
            } else {
                showPriceRange(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showPriceRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<PriceRange> listPriceRange = PriceRangeDAO.showPriceRange();
        request.setAttribute("priceranges", listPriceRange);
        request.getRequestDispatcher("PriceRangeManagement.jsp").forward(request, response);
    }

    protected void addPriceRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int priceFrom = Integer.parseInt(request.getParameter("priceFrom"));
        int priceTo = Integer.parseInt(request.getParameter("priceTo"));
        boolean rowUpdate = PriceRangeDAO.addPriceRange(priceFrom, priceTo);
        System.out.println("Loi r");
        if (rowUpdate) {
            showPriceRange(request, response);
        } else {
            System.out.println("Price Range add loi");
        }
    }

    protected void deletePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int priceRangeId = Integer.parseInt(request.getParameter("priceRangeID"));
            boolean rowUpdate = PriceRangeDAO.deletePriceRangeById(priceRangeId);
            if (rowUpdate) {
                showPriceRange(request, response);
            }else{
                System.out.println("Loi o update Range");
            }
        } catch (Exception e) {
        }
   
    }

    protected void updatePriceRange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int priceRangeID = Integer.parseInt(request.getParameter("priceRangeID"));
            int priceFrom = Integer.parseInt(request.getParameter("priceFrom"));
            int priceTo = Integer.parseInt(request.getParameter("priceTo"));
            PriceRange p = new PriceRange(priceRangeID, priceFrom, priceTo);
            boolean rowUpdate = PriceRangeDAO.UpdatePriceRangeById(p);
            if (rowUpdate) {
                showPriceRange(request, response);
            }else{
                System.out.println("Loi o update Range");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void searchPriceRange(HttpServletRequest request, HttpServletResponse response)
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
