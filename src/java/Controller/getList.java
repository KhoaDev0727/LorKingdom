/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.SuperCategory;
import Model.Category;
import Model.Age;
import Model.Sex;
import Model.PriceRange;
import Model.Brand;
import Model.Material;
import Model.Origin;
import Model.Product;
import Model.ProductImage;

import DAO.ProductDAO;
import DAO.ProductImageDAO;
import DAO.OriginDAO;
import DAO.MaterialDAO;
import DAO.BrandDAO;
import DAO.PriceRangeDAO;
import DAO.SexDAO;
import DAO.AgeDAO;
import DAO.SuperCategoryDAO;
import DAO.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author admin1
 */
public class getList extends HttpServlet {

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
            out.println("<title>Servlet getList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getList at " + request.getContextPath() + "</h1>");
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
        SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        AgeDAO ageDAO = new AgeDAO();
        SexDAO sexDAO = new SexDAO();
        PriceRangeDAO priceRangeDAO = new PriceRangeDAO();
        BrandDAO brandDAO = new BrandDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        OriginDAO originDAO = new OriginDAO();
        ProductDAO productDAO = new ProductDAO();
        ProductImageDAO productImageDAO = new ProductImageDAO();
        int itemsPerPage = 10; 
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int offset = (page - 1) * itemsPerPage;

        try {
         
            Integer categoryID = request.getParameter("categoryID") != null ? Integer.parseInt(request.getParameter("categoryID")) : null;
            Integer ageID = request.getParameter("ageID") != null ? Integer.parseInt(request.getParameter("ageID")) : null;
            Integer sexID = request.getParameter("sexID") != null ? Integer.parseInt(request.getParameter("sexID")) : null;
            Integer priceRangeID = request.getParameter("priceRangeID") != null ? Integer.parseInt(request.getParameter("priceRangeID")) : null;
            Integer brandID = request.getParameter("brandID") != null ? Integer.parseInt(request.getParameter("brandID")) : null;
            Integer materialID = request.getParameter("materialID") != null ? Integer.parseInt(request.getParameter("materialID")) : null;
            Integer originID = request.getParameter("originID") != null ? Integer.parseInt(request.getParameter("originID")) : null;
        
            boolean hasFilters = categoryID != null || ageID != null || sexID != null
                    || priceRangeID != null || brandID != null || materialID != null || originID != null;

            List<Product> products;
            if (hasFilters) {
           
                products = productDAO.getFilteredProducts(categoryID, ageID, sexID, priceRangeID, brandID, materialID, originID);
            } else {
                products = productDAO.getAllProducts();
            }

            int totalProducts = products.size();
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("listP", products);
            request.setAttribute("superCategories", superCategoryDAO.getAllSuperCategories());
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.setAttribute("ages", ageDAO.getAllAges());
            request.setAttribute("listS", sexDAO.getAllSexes());
            request.setAttribute("listB", brandDAO.getAllBrands());
            request.setAttribute("listM", materialDAO.getAllActiveMaterials());
            request.setAttribute("listO", originDAO.getAllActiveOrigins());
            request.setAttribute("listPriceRanges", priceRangeDAO.getAllActivePriceRanges());
            request.setAttribute("mainImages", ProductImageDAO.getMainProductImages());

            String partial = request.getParameter("partial");
            if ("true".equals(partial)) {
                request.getRequestDispatcher("productList.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
        processRequest(request, response);
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
