/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import Model.Review;
import java.util.ArrayList;

/**
 *
 * @author Truong Van Khang - CE181852
 */
public class ReviewManagementServlet extends HttpServlet {

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
            out.println("<title>Servlet ReviewManagementServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReviewManagementServlet at " + request.getContextPath() + "</h1>");
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
                        deleteReview(request, response);
                        break;
                    case "search":
                        searchSearch(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
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
                    case "update":
                        System.out.println("khang");
                        updateStatusReview(request, response);
                        break;
                    default:
                        showReview(request, response);
                }
            } else {
                showReview(request, response);
            }
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

    private void showReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Review> list = new ArrayList<>();
            list = ReviewDAO.showReview();
            request.setAttribute("reviews", list);
            request.getRequestDispatcher("ReviewManagement.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            boolean deleted = ReviewDAO.deleteReview(reviewID);
            if (deleted) {
                showReview(request, response);
            }else{
                System.out.println("sout");  
            }   
        } catch (Exception e) {
        }
    }

    private void searchSearch(HttpServletRequest request, HttpServletResponse response) {

    }

    private void updateStatusReview(HttpServletRequest request, HttpServletResponse response) {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            int Status = Integer.parseInt(request.getParameter("status"));
            boolean updateRow = ReviewDAO.UpdateStatusReview(reviewID, Status);
            if (updateRow) {
                showReview(request, response);
            } else {
                System.out.println("loi r");
            }
        } catch (Exception e) {
        }
    }

}
